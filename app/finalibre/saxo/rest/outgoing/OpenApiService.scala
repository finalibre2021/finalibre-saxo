package finalibre.saxo.rest.outgoing

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.OpenApiService._
import finalibre.saxo.rest.outgoing.responses.{ResponseAccount, ResponseAuthorizationToken, ResponseClient, ResponsePosition}
import finalibre.saxo.rest.outgoing.streaming.StreamingEndpoints.StreamingEndpoint
import finalibre.saxo.rest.outgoing.streaming.StreamingSubscription
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic
import org.slf4j.LoggerFactory
import responses.ServiceResult._
import play.api.libs.json.{JsObject, JsValue, Json, JsonConfiguration, Reads}
import play.api.libs.ws.{BodyWritable, WSAuthScheme, WSClient, WSRequest, WSResponse}
import play.api.mvc.Results.Redirect
import play.api.mvc.{AnyContent, Request, Result}

import java.net.URLEncoder
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class OpenApiService @Inject()(
                                client : WSClient,
                                execContext: ExecutionContext
                              ) {
  private implicit val executionContext = execContext
  private val logger = LoggerFactory.getLogger(this.getClass)

  def isConnected(context : OpenApiCallingContext) : Future[Boolean] = get("root/v1/sessions/capabilities", Some(context))(_ => true) map {
    case Right(_) => true
    case _ => false
  }

  def defaultClient(callingContext : OpenApiCallingContext) : Future[CallResult[ResponseClient]] =
    getAndRead("port/v1/clients/me", Some(callingContext))(ResponseClient.reads)

  def clients(callingContext : OpenApiCallingContext) : Future[CallResult[List[ResponseClient]]] = {
    defaultClient(callingContext).flatMap {
      case Left(err) => Future{ Left(err)  }
      case Right(client) => {
        logger.info(s"Got the default client: ${client.clientKey}")
        getAndRead("port/v1/clients", Some(callingContext), List("OwnerKey" -> client.clientKey))
      }
    }
  }


  def accounts(clientKey : String, callingContext : OpenApiCallingContext) : Future[CallResult[List[ResponseAccount]]] =
    (getAndRead( s"port/v1/accounts/?ClientKey=${enc(clientKey)}&IncludeSubAccounts=true", Some(callingContext)))

  def positions(accountGroupKey : Option[String], accountKey : Option[String], clientKey : String, callingContext : OpenApiCallingContext): Future[CallResult[List[ResponsePosition]]] = {
    val args = List(
      "AccountGroupKey" -> accountGroupKey,
      "AccountKey" -> accountKey,
      "ClientKey" -> Some(clientKey)) collect {case (nam, Some(value)) => nam -> value}
    getAndRead(s"port/v1/positions", Some(callingContext), args)
  }

  def exchangeCode(code : String) : Future[CallResult[ResponseAuthorizationToken]] =
    postQueryStringWithRead("token",None,List(
      "grant_type" -> "authorization_code",
      "code" -> code,
      "client_id" -> clientId),
      baseUrl = authorizationBaseUrl,
      useBasicAuthentication = true)(ResponseAuthorizationToken.reads)

  def apiDescription(area : String, callingContext : OpenApiCallingContext) : Future[CallResult[WSResponse]] = {
    perform(resp => resp.get)(s"openapi.yaml", Some(callingContext), Nil, "https://gateway.saxobank.com/sim", true)(resp => resp)
  }

  private def createSubscription[Topic <: StreamingTopic](endpoint : StreamingEndpoint[Topic], body : Product) : Future[CallResult[StreamingSubscription[Topic]]] = {
    import io.circe.syntax._
    import io.circe.generic.auto._
    val json = body.asJson
    val bodyFunc = ((req : WSRequest) => req.withBody(json.spaces2))
    val performResult = perform(req => req.post(json.spaces2))(endpoint.)

  }


  private[outgoing] def refreshToken(refreshToken : String) : Future[CallResult[ResponseAuthorizationToken]] =
    postQueryStringWithRead("token",None, List(
      "grant_type" -> "refresh_token",
      "refresh_token" -> refreshToken,
      "redirect_uri" -> dummyUrl),
      baseUrl = authorizationBaseUrl,
    useBasicAuthentication = true)(ResponseAuthorizationToken.reads)



  private def filterSuccess(resp : WSResponse) = resp.status match {
    case 401 => Left(AuthorizationError)
    case stat if stat >= 200 && stat < 300 => Right(resp)
    case stat => Left(OtherHttpStatusError(stat, resp.statusText))
  }


  private def get[A](endpoint : String, context : Option[OpenApiCallingContext], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl)(func : JsValue => A) : Future[CallResult[A]] =
    performJsonOperation(req => req.get())(endpoint,context, urlArguments, baseUrl, false)(func)

  private def getAndRead[A](endpoint : String, context : Option[OpenApiCallingContext], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    performAndRead(req => req.get())(endpoint,context, urlArguments, baseUrl)(reads)

  private def postQueryStringWithRead[A](endpoint : String, context : Option[OpenApiCallingContext], formData : List[(String, String)], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl, useBasicAuthentication : Boolean = false)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    {
      val body = buildParameterString(formData, true)
      logger.debug(s"POST: parameter body: $body")
      performAndRead(req => req
        .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
        .post(body)
      )(endpoint,context, urlArguments, baseUrl, useBasicAuthentication = useBasicAuthentication)(reads)
    }



  private def performAndRead[A](verb : WSRequest => Future[WSResponse])(endpoint : String, context : Option[OpenApiCallingContext], urlArguments : List[(String, String)] = Nil, baseUrl : String = openApiBaseUrl, useBasicAuthentication : Boolean = false)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    performJsonOperation(verb)(endpoint, context, urlArguments, baseUrl,useBasicAuthentication)((js : JsValue) => js.as[A])

  private def performJsonOperation[A](verb : WSRequest => Future[WSResponse])(endpoint : String, context : Option[OpenApiCallingContext], urlArguments : List[(String, String)], baseUrl : String, useBasicAuthentication : Boolean)(func : JsValue => A) : Future[CallResult[A]] =
    perform(verb)(endpoint, context, urlArguments, baseUrl, useBasicAuthentication)(
      resp => {
        val asJson = Json.parse(resp.body)
        func(asJson)
      }
    )

  private def perform[A](verb : WSRequest => Future[WSResponse])(endpoint : String, context : Option[OpenApiCallingContext], urlArguments : List[(String, String)], baseUrl : String, useBasicAuthentication : Boolean, extraFunc : Option[WSRequest => WSRequest] = None)(func : WSResponse => A) : Future[CallResult[A]] = {
    Try {
      val url = if(baseUrl.endsWith("/")) s"$baseUrl$endpoint" else s"$baseUrl/$endpoint"
      logger.info(s"Accessing URL: $url, with URL parameters: ${urlArguments.map(p => p._1 + "=" + p._2)}")
      var req = client
        .url(url)
        .withQueryStringParameters(urlArguments : _ *)
      context.foreach {
        case ctx => req = req.withHttpHeaders(OpenApiService.tokenAuthPair(ctx.token))
      }
      if(useBasicAuthentication)
        req = req.withAuth(clientId, clientSecret, WSAuthScheme.BASIC)

      extraFunc.foreach {
        case func => req = func(req)
      }

      verb(req).map(res => filterSuccess(res) match {
        case Right(resp) =>
          Try {
            logger.info(s"Going to call map function for response: ${resp.toString}")
            val funcRes = func(resp)
            funcRes
          }
          match {
            case Success(v) => Right(v)
            case Failure(err) => Left(ExceptionError(err))
          }
        case left @ Left(err) => Left(err)
      })

    }
  } match {
    case Failure(err) => Future { Left(ExceptionError(err))}
    case Success(fut) => fut
  }



}

object OpenApiService {
  val AuthorizationHeaderName = "Authorization"
  private val openApiBaseUrl = SaxoConfig.Rest.Outgoing.openApiBaseUrl
  private val authorizationBaseUrl = SaxoConfig.Rest.Outgoing.authenticationBaseUrl
  private val clientId = SaxoConfig.Rest.Outgoing.clientId
  private val clientSecret = SaxoConfig.Rest.Outgoing.clientSecret
  private val dummyUrl = "https://localhost/index"

  def buildAuthorizationRedirect(state : String, callbackUrl : String) : Result = {
    val conf = SaxoConfig.Rest.Outgoing
    val url = s"${authorizationBaseUrl}/authorize?response_type=code&client_id=${enc(conf.clientId)}&state=${enc(state)}&redirect_uri=${enc(callbackUrl)}"
    Redirect(url)
      .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")
  }

  private def tokenAuthPair(token : String) : (String, String) =  OpenApiService.AuthorizationHeaderName -> s"BEARER ${token}"

  private def buildParameterString(map : List[(String, String)], encode : Boolean) = map
    .map(p => p._1 + "=" + (if(encode) enc(p._2) else p._2))
    .mkString("&")

  private def enc = (str : String) => URLEncoder.encode(str, "UTF-8")





}
