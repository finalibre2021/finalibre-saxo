package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.SaxoAuthenticator
import finalibre.saxo.security.{Encryptor, SessionRepository}
import org.slf4j.LoggerFactory
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, Result}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FinaLibreController @Inject()(
                                     cc : ControllerComponents,
                                     executionContext : ExecutionContext,
                                     sessionRepository: SessionRepository,
                                     encryptor : Encryptor,
                                     saxoAuthenticator: SaxoAuthenticator
                                   )(implicit inSys : ActorSystem, inMat : Materializer) extends AbstractController(cc) {
  protected lazy val logger = LoggerFactory.getLogger(this.getClass)
  private implicit val ec = executionContext

  def index = actionFrom {
    case (request : Request[AnyContent], context) => {
      Ok(s"All good... Context session ID: ${context.sessionId}")
    }

  }

  def actionFrom(act : (Request[AnyContent], FinaLibreController.FinaLibreControllerContext) => Result) : Action[AnyContent] = Action.async {
    request: Request[AnyContent] =>
      authenticate(request) match {
        case Left(fut) => fut
        case Right(contx) => Future {
          act(request, contx)
        }
      }
  }

  def authenticate(request : Request[AnyContent]) : Either[Future[Result], FinaLibreController.FinaLibreControllerContext] = {
    implicit val req = request
    val ip = request.connection.remoteAddress.getHostAddress
    val forwardUrl = request.uri
    request.cookies.get(FinaLibreController.AdminSessionCookie) match {
      case Some(cook) => {
        val sessionId = cook.value
        if(sessionRepository.isAuthorized(sessionId, ip)) Right(FinaLibreController.FinaLibreControllerContext(sessionId))
        else Left(initiateAuthenticationFlow(ip,forwardUrl))
      }
      case _ => Left(initiateAuthenticationFlow(ip, forwardUrl))
    }
  }

  def initiateAuthenticationFlow(ip : String, forwardUrl : String)(implicit req : Request[AnyContent]) = {
    val (sessionId, nonce) = sessionRepository.nextIdAndNonce()
    val state = encryptor.serializeAndEncrypt(
      List(
        FinaLibreController.StateFieldSessionIdName -> sessionId,
        FinaLibreController.StateFieldIpName -> ip,
        FinaLibreController.StateFieldNonceName -> nonce
      )
    )
    logger.info(s"Sending state: $state")
    val callbackUrl = AuthenticationCallbackController.urlToCallback
    logger.info(s"Directing to forward URL: $callbackUrl")
    sessionRepository.initiateAuthenticationProcess(sessionId, ip, nonce, state, forwardUrl)
    val redirectResult = saxoAuthenticator.buildRedirect(state)
    Future {
      redirectResult
    }
  }

}

object FinaLibreController {
  case class FinaLibreControllerContext(sessionId : String)

  val AdminSessionCookie = "SESSIONID"
  val StateFieldSessionIdName = "SessionId"
  val StateFieldIpName = "Ip"
  val StateFieldNonceName = "Nonce"
}
