package finalibre.saxo.rest.outgoing

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.responses.ResponseClient
import finalibre.saxo.security.SessionRepository
import responses.ServiceResult._
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.libs.ws.{BodyWritable, WSClient, WSRequest, WSResponse}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class OpenApiService @Inject()(
                                client : WSClient,
                                execContext: ExecutionContext,
                                sessionRepository : SessionRepository
                              ) {

  private implicit val executionContext = execContext
  private val baseUrl = SaxoConfig.Rest.Outgoing.openApiBaseUrl

  def isConnected(token : String) : Future[Boolean] = get("root/v1/sessions/capabilities", token)(_ => true) map {
    case Right(_) => true
    case _ => false
  }

  def defaultClient()(token : String) : Future[CallResult[ResponseClient]] = get("port/v1/clients/me", token)(json => ResponseClient(
    (json \ "ClientId").as[String],
    (json \ "ClientKey").as[String],
    (json \ "DefaultAccountId").as[String],
    (json \ "DefaultAccountKey").as[String],
    (json \ "DefaultCurrency").as[String],
    (json \ "Name").as[String],
    (json \ "PartnerPlatformId").asOpt[String]
  )

  )

  private def urlFor(endpoint : String, urlArguments : List[(String, String)] = Nil) : String = {
    val url = s"${baseUrl}/${endpoint}${if(urlArguments.isEmpty) "" else "?"}"
    val returnee = urlArguments.foldLeft(url)((curr, p) => curr + (if(curr.endsWith("&")) "" else "&") + p._1 + "=" + p._2)
    returnee
  }

  private def filterSuccess(resp : WSResponse) = resp.status match {
    case 401 => Left(AuthorizationError)
    case stat if stat >= 200 && stat < 300 => Right(resp)
    case stat => Left(OtherHttpStatusError(stat, resp.statusText))
  }

  private def asJson(resp : WSResponse) = Try {
    Json.parse(resp.body)
  }


  private def get[A](endpoint : String, token : String, urlArguments : List[(String, String)]= Nil, setAuthorizationHeader : Boolean = true)(func : JsValue => A) : Future[CallResult[A]] =
    perform(req => req.get(), setAuthorizationHeader)(endpoint,token, urlArguments)(func)

  private def post[A](endpoint : String, token : String, formData : Map[String, Seq[String]], urlArguments : List[(String, String)]= Nil, setAuthorizationHeader : Boolean = true)(func : JsValue => A) : Future[CallResult[A]] =
    perform(req => req.post(formData), setAuthorizationHeader)(endpoint,token, urlArguments)(func)

  private def post[A](endpoint : String, token : String, jsonData : JsObject, urlArguments : List[(String, String)]= Nil, setAuthorizationHeader : Boolean = true)(func : JsValue => A) : Future[CallResult[A]] =
    perform(req => req.post(jsonData), setAuthorizationHeader)(endpoint,token, urlArguments)(func)


  private def perform[A](verb : WSRequest => Future[WSResponse], setAuthorizationHeader : Boolean)(endpoint : String, token : String, urlArguments : List[(String, String)] = Nil)(func : JsValue => A) : Future[CallResult[A]] = {
    Try {
      var req = client.url(urlFor(endpoint, urlArguments))
      if(setAuthorizationHeader)
        req = req.withHttpHeaders(OpenApiService.AuthorizationHeaderName -> s"BEARER $token")
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


  def refreshToken(token : String) : CallResult[String] = Try {
    sessionRepository.liveSaxoRefreshTokenFor(token) match {
      case Some(refr) => {
        val resp = post()
      }
    }
  }




}

object OpenApiService {
  val AuthorizationHeaderName = "Authorization"
}
