package finalibre.saxo.rest.outgoing

import controllers.AuthenticationCallbackController
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.responses.{ResponseAuthorizationToken, ResponseClient}
import finalibre.saxo.security.SessionRepository
import org.slf4j.LoggerFactory
import responses.ServiceResult._
import play.api.libs.json.{JsObject, JsValue, Json, Reads}
import play.api.libs.ws.{BodyWritable, WSAuthScheme, WSClient, WSRequest, WSResponse}
import play.api.mvc.Results.Redirect
import play.api.mvc.{AnyContent, Request, Result}

import java.net.URLEncoder
import java.util.Base64
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class OpenApiService @Inject()(
                                client : WSClient,
                                execContext: ExecutionContext
                              ) {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private implicit val executionContext = execContext
  private val openApiBaseUrl = SaxoConfig.Rest.Outgoing.openApiBaseUrl
  private val authorizationBaseUrl = SaxoConfig.Rest.Outgoing.authenticationBaseUrl
  private val clientId = SaxoConfig.Rest.Outgoing.clientId
  private val clientSecret = SaxoConfig.Rest.Outgoing.clientSecret
  private val dummyUrl = "https://localhost/index"

  def isConnected(token : String) : Future[Boolean] = get("root/v1/sessions/capabilities", Some(token))(_ => true) map {
    case Right(_) => true
    case _ => false
  }

  def defaultClient()(token : String) : Future[CallResult[ResponseClient]] = getAndRead("port/v1/clients/me", Some(token))(ResponseClient.reads)

  def exchangeCode(code : String) : Future[CallResult[ResponseAuthorizationToken]] =
    postQueryStringWithRead("token",None,List(
      "grant_type" -> "authorization_code",
      "code" -> code,
      "client_id" -> clientId),
      baseUrl = authorizationBaseUrl,
      useBasicAuthentication = true)(ResponseAuthorizationToken.reads)

  def refreshToken(refreshToken : String, currentToken : String) : Future[CallResult[ResponseAuthorizationToken]] =
    postQueryStringWithRead("token",None, List(
      "grant_type" -> "refresh_token",
      "refresh_token" -> refreshToken,
      "redirect_uri" -> dummyUrl),
      baseUrl = authorizationBaseUrl,
    useBasicAuthentication = true)(ResponseAuthorizationToken.reads)


  def buildAuthorizationRedirect(state : String, callbackUrl : String) : Result = {
    val conf = SaxoConfig.Rest.Outgoing
    val url = s"${authorizationBaseUrl}/authorize?response_type=code&client_id=${enc(conf.clientId)}&state=${enc(state)}&redirect_uri=${enc(callbackUrl)}"
    Redirect(url)
      .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")
  }


  private def filterSuccess(resp : WSResponse) = resp.status match {
    case 401 => Left(AuthorizationError)
    case stat if stat >= 200 && stat < 300 => Right(resp)
    case stat => Left(OtherHttpStatusError(stat, resp.statusText))
  }

  private def asJson(resp : WSResponse) = Try {
    Json.parse(resp.body)
  }


  private def get[A](endpoint : String, token : Option[String], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl)(func : JsValue => A) : Future[CallResult[A]] =
    perform(req => req.get())(endpoint,token, urlArguments, baseUrl, false)(func)

  private def getAndRead[A](endpoint : String, token : Option[String], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    performAndRead(req => req.get())(endpoint,token, urlArguments, baseUrl)(reads)

  private def postQueryStringWithRead[A](endpoint : String, token : Option[String], formData : List[(String, String)], urlArguments : List[(String, String)]= Nil, baseUrl : String = openApiBaseUrl, useBasicAuthentication : Boolean = false)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    {
      val body = buildParameterString(formData, true)
      logger.debug(s"POST: parameter body: $body")
      performAndRead(req => req
        .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
        .post(body)
      )(endpoint,token, urlArguments, baseUrl, useBasicAuthentication = useBasicAuthentication)(reads)
    }



  private def performAndRead[A](verb : WSRequest => Future[WSResponse])(endpoint : String, token : Option[String], urlArguments : List[(String, String)] = Nil, baseUrl : String = openApiBaseUrl, useBasicAuthentication : Boolean = false)(implicit reads : Reads[A]) : Future[CallResult[A]] =
    perform(verb)(endpoint, token, urlArguments, baseUrl,useBasicAuthentication)((js : JsValue) => js.as[A])

  private def perform[A](verb : WSRequest => Future[WSResponse])(endpoint : String, token : Option[String], urlArguments : List[(String, String)], baseUrl : String, useBasicAuthentication : Boolean)(func : JsValue => A) : Future[CallResult[A]] = {
    Try {
      val url = if(baseUrl.endsWith("/")) s"$baseUrl$endpoint" else s"$baseUrl/$endpoint"
      logger.info(s"Accessing URL: $url, with URL parameters: ${urlArguments.map(p => p._1 + "=" + p._2.mkString(","))}")
      var req = client
        .url(url)
        .withQueryStringParameters(urlArguments : _ *)
      token.foreach {
        case token => req = req.withHttpHeaders(OpenApiService.AuthorizationHeaderName -> s"BEARER $token")
      }
      if(useBasicAuthentication)
        req = req.withAuth(clientId, clientSecret, WSAuthScheme.BASIC)

      verb(req).map(res => filterSuccess(res) match {
          case Right(resp) => {
            val toRet = (for (
              js <- asJson(resp);
              res <- Try {
                func(js)
              }
            ) yield res)
            toRet
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


  private def buildParameterString(map : List[(String, String)], encode : Boolean) = map
    .map(p => p._1 + "=" + (if(encode) enc(p._2) else p._2))
    .mkString("&")

  private def enc = (str : String) => URLEncoder.encode(str, "UTF-8")



}

object OpenApiService {
  val AuthorizationHeaderName = "Authorization"
}
