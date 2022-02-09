package controllers

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.OpenApiCallingContext.OpenApiFunction
import finalibre.saxo.rest.outgoing.{OpenApiCallingContext, OpenApiService}
import finalibre.saxo.rest.outgoing.responses.ServiceResult
import finalibre.saxo.rest.outgoing.responses.ServiceResult.CallResult
import finalibre.saxo.security.{Encryptor, SessionRepository}
import finalibre.saxo.util.DateExtensions.ExtendedLocalDateTime
import finalibre.saxo.util.Pingable
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.JsObject
import play.api.libs.streams.ActorFlow
import play.api.libs.ws._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, RequestHeader, Result, WebSocket}

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class FinaLibreController @Inject()(
                                     cc : ControllerComponents,
                                     executionContext : ExecutionContext,
                                     sessionRepository: SessionRepository,
                                     encryptor : Encryptor,
                                     openApiService: OpenApiService
                                   )(implicit inSys : ActorSystem, inMat : Materializer) extends AbstractController(cc) {
  private lazy val logger = LoggerFactory.getLogger(this.getClass)
  implicit val executionContextImplicit = executionContext
  implicit val apiServiceImplicit = openApiService
  implicit val sessionRepositoryImplicit = sessionRepository


  def index = actionFrom {
    case (request : Request[AnyContent], context) => {
      callOn((api,cont) => api.defaultClient(cont), context.sessionId).map {
        case Left(err) => Ok(err.toString)
        case Right(client) => Redirect(routes.PositionsController.positionsIndex())
      }
    }
  }

  def wsFrom(actorCreator : (ActorRef, OpenApiCallingContext) => Actor) = WebSocket.acceptOrResult[Any, JsObject] {
    case request => {
      Future.successful {
        val contextOpt = for(
          cook <- request.cookies.get(FinaLibreController.AdminSessionCookie);
          token <- sessionRepository.liveSaxoToken(cook.value)
        ) yield OpenApiCallingContext(cook.value, token)
        contextOpt match {
          case Some(cont) => Right(ActorFlow.actorRef {
            case out => Props(actorCreator(out, cont))
          } )
          case None =>Left(Forbidden)
        }
      }
    }
  }

  def callOn[A](call : OpenApiFunction[A], sessionId : String) : Future[CallResult[A]] =
    OpenApiCallingContext.call(call,sessionId)

  def actionFrom(act : (Request[AnyContent], OpenApiCallingContext) => Future[Result]) : Action[AnyContent] = Action.async {
    request: Request[AnyContent] =>
      authenticate(request) match {
        case Left(fut) => fut
        case Right(contx) =>
          act(request, contx)
      }
  }

  def authenticate(request : Request[AnyContent]) : Either[Future[Result], OpenApiCallingContext] = {
    implicit val req = request
    val ip = request.connection.remoteAddress.getHostAddress
    val forwardUrl = request.uri
    request.cookies.get(FinaLibreController.AdminSessionCookie) match {
      case Some(cook) => {
        val sessionId = cook.value
        (sessionRepository.isAuthorized(sessionId, ip), sessionRepository.liveSaxoToken(sessionId)) match {
          case (true, Some(token)) => Right(OpenApiCallingContext(sessionId, token))
          case _ => Left(initiateAuthenticationFlow(ip,forwardUrl))
        }
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
    val redirectResult = OpenApiService.buildAuthorizationRedirect(state, callbackUrl)
    Future {
      redirectResult
    }
  }

  abstract class FinaLibreWSActor(out : ActorRef, sessionId : String) extends Actor with Pingable {
    override def pingOut: ActorRef = out
  }

}

object FinaLibreController {
  case class LockEntry(sessionId : String) {
    val lock = new {}
    var lastUsed = LocalDateTime.now()
  }
  val AdminSessionCookie = "SESSIONID"
  val StateFieldSessionIdName = "SessionId"
  val StateFieldIpName = "Ip"
  val StateFieldNonceName = "Nonce"


  def wrapAction(logger : Logger)(message : Any)(action : PartialFunction[Any, Any]) = Try {
    action(message)
  } match {
    case Failure(err) => logger.error(s"Error occured during handling of request with body: ${message.toString}")
    case Success(res) => logger.debug(s"Succesfully handled request: ${message.toString}")
  }

}
