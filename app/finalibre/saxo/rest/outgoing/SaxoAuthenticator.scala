package finalibre.saxo.rest.outgoing

import finalibre.saxo.configuration.SaxoConfig
import play.api.libs.ws.WSClient
import play.api.mvc._
import play.api.mvc.Results.Redirect

import java.net.URLEncoder
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SaxoAuthenticator @Inject() (client : WSClient) {

  def buildRedirect(redirectUrl : String, nonce : String, state : String)(implicit executionContext: ExecutionContext) : Result = {
    val conf = SaxoConfig.Rest.Outgoing
    val enc = encodeUrlParameter _
    val url = s"${conf.authenticationUrl}?response_type=code&client_id=${enc(conf.clientId)}&state=${enc(state)}&redirect_uri=${enc(redirectUrl)}"
    Redirect(url)
      .withHeaders("Content-Type" -> "application/x-www-form-urlencoded")
  }

  def exchangeCode(code : String, redirectUrl : String)(implicit executionContext: ExecutionContext) = {
    val conf = SaxoConfig.Rest.Outgoing
    val enc = encodeUrlParameter _
    val parameterString = s"grant_type=authorization_code&code=${code}&client_id=${conf.clientId}&client_secret=${conf.clientSecret}&redirect_uri=${redirectUrl}"
    client
      .url(conf.tokenUrl)
      .withHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .post(parameterString)


  }

  private def encodeUrlParameter(str : String) = URLEncoder.encode(str, "utf-8")


}
