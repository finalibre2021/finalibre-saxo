package finalibre.saxo.rest.outgoing

import controllers.AuthenticationCallbackController
import finalibre.saxo.configuration.SaxoConfig
import org.slf4j.LoggerFactory
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.mvc.Results.Redirect

import java.net.URLEncoder
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SaxoAuthenticator @Inject() (client : WSClient) {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def buildRedirect(state : String)(implicit executionContext: ExecutionContext, request: Request[AnyContent]) : Result = {
    val conf = SaxoConfig.Rest.Outgoing
    val enc = encodeUrlParameter _
    val callbackUrl = AuthenticationCallbackController.urlToCallback
    val url = s"${conf.authenticationUrl}?response_type=code&client_id=${enc(conf.clientId)}&state=${enc(state)}&redirect_uri=${enc(callbackUrl)}"
    Redirect(url)
      .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")

  }

  def exchangeCode(code : String)(implicit executionContext: ExecutionContext, request: Request[AnyContent]) = {
    val conf = SaxoConfig.Rest.Outgoing
    val enc = encodeUrlParameter _
    val dummyRedirectUrl = AuthenticationCallbackController.urlToCallback
    val parameterString = s"grant_type=authorization_code&code=${code}&client_id=${conf.clientId}&client_secret=${conf.clientSecret}&redirect_uri=${dummyRedirectUrl}"
    client
      .url(conf.tokenUrl)
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .post(parameterString)
      .map {
        case resp => {
          val bodyString = resp.body
          logger.info(s"Exchanging code status: ${resp.status} : ${resp.statusText}")
          logger.info(s"Body string from exchanging code: $bodyString")
          SaxoTokenReply.parseSaxoReplyBody(bodyString)
        }
      }


  }

  private def encodeUrlParameter(str : String) = URLEncoder.encode(str, "utf-8")


}
