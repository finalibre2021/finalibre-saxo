package controllers

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.rest.outgoing.responses.ServiceResult
import finalibre.saxo.rest.outgoing.responses.ServiceResult.CallResult
import finalibre.saxo.security.{Encryptor, SessionRepository}
import finalibre.saxo.util.DateExtensions.ExtendedLocalDateTime
import finalibre.saxo.util.Pingable
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.JsObject
import play.api.libs.streams.ActorFlow
import play.api.libs.ws._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, Result, WebSocket}

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
  protected lazy val logger = LoggerFactory.getLogger(this.getClass)
  private implicit val ec = executionContext

  def index = actionFrom {
    case (request : Request[AnyContent], context) => {
      implicit val contx = context
      callOn(serv => serv.defaultClient()(context.token)).map {
        case Left(err) => Ok(err.toString)
        case Right(client) => Ok(s"Logged in client ID: ${client.clientId}, client key: ${client.clientKey}, name: ${client.name}")
      }
    }
  }

  def wsFrom(actorCreator : (ActorRef) => Actor) = WebSocket.acceptOrResult[Any, JsObject] {
    case request => {
      Future.successful {
        Right(ActorFlow.actorRef {
          case out => Props(actorCreator(out))
        } )
      }
    }
  }

  def callOn[A](call : OpenApiService => Future[CallResult[A]])(implicit finaLibreContext : FinaLibreController.FinaLibreControllerContext) : Future[CallResult[A]] =
    FinaLibreController.callOn(call, openApiService, sessionRepository)

  def actionFrom(act : (Request[AnyContent], FinaLibreController.FinaLibreControllerContext) => Future[Result]) : Action[AnyContent] = Action.async {
    request: Request[AnyContent] =>
      authenticate(request) match {
        case Left(fut) => fut
        case Right(contx) =>
          act(request, contx)
      }
  }

  def authenticate(request : Request[AnyContent]) : Either[Future[Result], FinaLibreController.FinaLibreControllerContext] = {
    implicit val req = request
    val ip = request.connection.remoteAddress.getHostAddress
    val forwardUrl = request.uri
    request.cookies.get(FinaLibreController.AdminSessionCookie) match {
      case Some(cook) => {
        val sessionId = cook.value
        (sessionRepository.isAuthorized(sessionId, ip), sessionRepository.liveSaxoToken(sessionId)) match {
          case (true, Some(token)) => Right(FinaLibreController.FinaLibreControllerContext(sessionId, token))
          case _ => Left(initiateAuthenticationFlow(ip,forwardUrl)) // TODO: Implement handling of refresh
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
    def callApiOn = callOn(api => api.accounts("")(""))
  }

}

object FinaLibreController {
  private val logger = LoggerFactory.getLogger(this.getClass)
  case class FinaLibreControllerContext(sessionId : String, token : String) {
  }
  case class LockEntry(sessionId : String) {
    val lock = new {}
    var lastUsed = LocalDateTime.now()
  }
  val AdminSessionCookie = "SESSIONID"
  val StateFieldSessionIdName = "SessionId"
  val StateFieldIpName = "Ip"
  val StateFieldNonceName = "Nonce"
  val SessionLocks = scala.collection.mutable.HashMap.empty[String, LockEntry]
  val SessionLocksLock = new {}

  def cleanUpUnusedSessionLocks() = {
    val threshold = LocalDateTime.now().minusMinutes(SaxoConfig.tokenLockLifeSpanInMinutes)
    var removed = 0
    SessionLocksLock.synchronized {
      val toRemove = SessionLocks.filter(lock => lock._2.lastUsed.isBefore(threshold)).map(_._1)
      toRemove.foreach(rem => SessionLocks.remove(rem))
      removed = toRemove.size
    }
    if(removed > 0) {
      logger.info(s"Removed $removed un-used token locks using threshold: ${threshold.toDanishDateTimeString}")
    }
  }

  def refreshRelevantConnections(sessionRepository: SessionRepository, openApiService: OpenApiService)(implicit executionContext: ExecutionContext) : Unit = {
    val processesToRefresh = sessionRepository.processesToRefreshTokenFor
    SessionLocksLock.synchronized {
      val liveProcesses = processesToRefresh.filter(proc => SessionLocks.contains(proc.sessionId))
      if (!liveProcesses.isEmpty) {
        val startTime = LocalDateTime.now()
        var refreshed = 0
        for(
          proc <- liveProcesses;
          currentToken <- proc.saxoAccessToken;
          refreshToken <- proc.saxoRefreshToken
        ) {
          openApiService.refreshToken(refreshToken).map {
            case Right(tokenData) => {
              sessionRepository.updateSaxoTokenDataByCurrentToken(proc.sessionId, currentToken, tokenData.accessToken, tokenData.expiresAt, tokenData.refreshToken, tokenData.refreshExpiresAt)
              logger.info(s"Refreshed token for session with ID: ${proc.sessionId}")
              refreshed += 1
            }
            case Left(ServiceResult.ExceptionError(err)) => logger.error(s"Error during token refresh for session with ID: ${proc.sessionId}", err)
            case Left(oth) => logger.error(s"Error during token refresh for session with ID: ${proc.sessionId}: $oth")
          }
        }
        val endTime = LocalDateTime.now()
        val (minutesExpired, secondsExpired) = startTime.minutesAndSecondsBetween(endTime)
        logger.info(s"Refreshed $refreshed tokens in $minutesExpired minutes and $secondsExpired seconds")
      }
    }
  }

  def callOn[A](call : OpenApiService => Future[CallResult[A]], openApiService: OpenApiService, sessionRepository: SessionRepository)(implicit finaLibreContext : FinaLibreController.FinaLibreControllerContext, executionContext: ExecutionContext) : Future[CallResult[A]] = {
    val sessionId = finaLibreContext.sessionId
    val token = finaLibreContext.token
    var lockObjectOpt : Option[FinaLibreController.LockEntry] = None
    FinaLibreController.SessionLocksLock.synchronized {
      lockObjectOpt = FinaLibreController.SessionLocks.get(sessionId)
      if(lockObjectOpt.isEmpty) {
        val newLockObj = FinaLibreController.LockEntry(sessionId)
        FinaLibreController.SessionLocks(sessionId) = newLockObj
        lockObjectOpt = Some(newLockObj)
      }
    }
    lockObjectOpt.get.lock.synchronized {
      lockObjectOpt.get.lastUsed = LocalDateTime.now()
      call(openApiService).flatMap {
        case success @ Right(value) => Future{ success }
        case Left(ServiceResult.AuthorizationError) => {
          sessionRepository.liveSaxoRefreshTokenFor(finaLibreContext.token) match {
            case Some(refreshToken) => {
              openApiService.refreshToken(refreshToken).flatMap {
                case Right(tokenReply) => {
                  sessionRepository.updateSaxoTokenDataByCurrentToken(sessionId, token, tokenReply.accessToken, tokenReply.expiresAt, tokenReply.refreshToken, tokenReply.refreshExpiresAt)
                  callOn(call, openApiService, sessionRepository)
                }
                case Left(err) => Future { Left(err) }
              }
            }
            case _ => Future{ Left(ServiceResult.OtherError("Access token not valid and no live refresh token exists")) }
          }
        }
        case Left(err) => Future{ Left(err) }
      }
    }
  }


  def wrapAction(logger : Logger)(message : Any)(action : PartialFunction[Any, Any]) = Try {
    action(message)
  } match {
    case Failure(err) => logger.error(s"Error occured during handling of request with body: ${message.toString}")
    case Success(res) => logger.debug(s"Succesfully handled request: ${message.toString}")
  }



}
