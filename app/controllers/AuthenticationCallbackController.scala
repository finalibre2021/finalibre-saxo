package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.rest.outgoing.responses.ServiceResult
import finalibre.saxo.security.{Encryptor, SessionRepository}
import org.slf4j.LoggerFactory
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthenticationCallbackController @Inject()(
                                                  cc : ControllerComponents,
                                                  executionContext : ExecutionContext,
                                                  encryptor : Encryptor,
                                                  sessionRepository: SessionRepository,
                                                  openApiService: OpenApiService

                                                )(implicit inSys : ActorSystem,
                                                  inMat : Materializer) extends AbstractController(cc){
  implicit val ec = executionContext
  private val logger = LoggerFactory.getLogger(this.getClass)

  def authenticationRequestCallback(state : String, code : String) : Action[AnyContent] = Action.async {
    implicit request : Request[AnyContent] => {
      logger.debug(s"Got request and with state: $state and code: $code")
      val stateMap = encryptor.decryptAndDeserialize(state).toMap
      logger.debug(s"State map: ${stateMap.toList.map(p => p._1 + ":" + p._2).mkString("\r\n")}")
      (stateMap.get(FinaLibreController.StateFieldSessionIdName), stateMap.get(FinaLibreController.StateFieldIpName), stateMap.get(FinaLibreController.StateFieldNonceName)) match {
        case (Some(sessionId), Some(ip), Some(nonce)) if sessionRepository.isValidNonceAndState(sessionId, nonce, state) => {
          logger.info(s"Extracted from state map on callback: sessionId: $sessionId, IP: $ip, nonce: $nonce")
          openApiService.exchangeCode(code).map {
            case Left(err) => {
              logger.error(s"Failed to exchange code for token. Code: ${code}")
              err match {
                case ServiceResult.ExceptionError(throwable) => logger.error("Error from exchange of token", throwable)
                case oth => logger.error(s"Error from exchange of token: ${oth}")
              }
              Results.BadGateway
            }
            case Right(resp) => {
              logger.info(s"Exchanging code to token succeeded. access_token: ${resp.accessToken}")
              sessionRepository.updateSaxoTokenData(sessionId, nonce, resp.accessToken,resp.expiresAt, resp.refreshToken, resp.refreshExpiresAt)
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
  def urlToCallback(implicit request : Request[_]) = routes.AuthenticationCallbackController.authenticationRequestCallback("","").absoluteURL(true).replaceAll("""\?.*""","")

}

