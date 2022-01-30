package finalibre.saxo.rest.outgoing

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.responses.ResponseClient
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.libs.ws.WSResponse

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class OpenApiService @Inject()(
                                client : WSClient,
                                execContext: ExecutionContext
                              ) {

  private implicit val executionContext = execContext
  private val baseUrl = SaxoConfig.Rest.Outgoing.openApiBaseUrl

  def isConnected(token : String) : Future[Boolean] = get("root/v1/sessions/capabilities", token)(_ => true) map {
    case Right(_) => true
    case _ => false
  }

  def defaultClient()(token : String) : Future[Either[String, ResponseClient]] = get("port/v1/clients/me", token)(json => ResponseClient(
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

  private def filterSuccess[A](resp : A, statusCode : Int) = if(statusCode >= 200 && statusCode < 300) Some(resp) else None

  private def asJson(resp : WSResponse) = Try {
    Json.parse(resp.body)
  }


  private def get[A](endpoint : String, token : String, urlArguments : List[(String, String)] = Nil)(func : JsValue => A) : Future[Either[String, A]] = {
    Try {
      client
        .url(urlFor(endpoint, urlArguments))
        .withHttpHeaders(OpenApiService.AuthorizationHeaderName -> s"BEARER $token")
        .get()
        .map(res => filterSuccess(res, res.status) match {
          case Some(resp) => {
            (for(
              js <- asJson(resp);
              res <- Try{func(js) }
            ) yield res)
            match {
              case Success(v) => Right(v)
              case Failure(err) => Left(err.getMessage)
            }
          }
          case _ => Left(res.statusText)
        })
    }
  } match {
    case Failure(err) => Future { Left(err.getMessage)}
    case Success(fut) => fut
  }




}

object OpenApiService {
  val AuthorizationHeaderName = "Authorization"
}
