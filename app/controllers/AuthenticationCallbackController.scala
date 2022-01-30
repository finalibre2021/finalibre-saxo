package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.SaxoAuthenticator
import finalibre.saxo.security.{Encryptor, SessionRepository}
import org.slf4j.LoggerFactory
import play.api.libs.ws.WSClient
import play.api.mvc._

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class AuthenticationCallbackController @Inject()(
                                                  cc : ControllerComponents,
                                                  executionContext : ExecutionContext,
                                                  wsClient : WSClient,
                                                  encryptor : Encryptor,
                                                  sessionRepository: SessionRepository,
                                                  saxoAuthenticator: SaxoAuthenticator
                                                )(implicit inSys : ActorSystem,
                                                  inMat : Materializer) extends AbstractController(cc){
  implicit val ec = executionContext
  private val logger = LoggerFactory.getLogger(this.getClass)

  def authorizationRequestCallback(state : String, code : String) : Action[AnyContent] = Action.async {
    implicit request : Request[AnyContent] => {
      logger.debug(s"Got request and with state: $state and code: $code")
      val stateMap = encryptor.decryptAndDeserialize(state).toMap
      logger.debug(s"State map: ${stateMap.toList.map(p => p._1 + ":" + p._2).mkString("\r\n")}")
      (stateMap.get(FinaLibreController.StateFieldSessionIdName), stateMap.get(FinaLibreController.StateFieldIpName), stateMap.get(FinaLibreController.StateFieldNonceName)) match {
        case (Some(sessionId), Some(ip), Some(nonce)) if sessionRepository.isValidNonceAndState(sessionId, nonce, state) => {
          logger.info(s"Extracted from state map on callback: sessionId: $sessionId, IP: $ip, nonce: $nonce")
          saxoAuthenticator.exchangeCode(code).map {
            case None => Results.BadGateway
            case Some(token) => {
              logger.info(s"Exchanging code to token succeeded. access_token: ${token.accessToken}")
              sessionRepository.updateSaxoTokenData(sessionId, nonce, token.accessToken,token.expiresAt, token.refreshToken, token.refreshExpiresAt)
              val sessionCookie = Cookie(FinaLibreController.AdminSessionCookie, sessionId)
              sessionRepository.forwardUrlFor(sessionId, nonce) match {
                case None => Ok("Oooops.... Don't know where to send ya").withCookies(sessionCookie).bakeCookies()
                case Some(url) => Redirect(url).withCookies(sessionCookie).bakeCookies()
              }
            }
          }
        }
        case _ => {
          logger.info(s"Could not match state map: ${stateMap.toList.map(p => p._1 + ":" + p._2).mkString(",")}")
          Future {Results.Forbidden}
        }
      }
    }
  }


}

object AuthenticationCallbackController {
  def urlToCallback(implicit request : Request[_]) = routes.AuthenticationCallbackController.callback("","").absoluteURL(true).replaceAll("""\?.*""","")

}

