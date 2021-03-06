package finalibre.saxo.rest.outgoing

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.responses.ServiceResult
import finalibre.saxo.rest.outgoing.responses.ServiceResult.{AuthorizationError, CallResult}
import finalibre.saxo.security.SessionRepository
import finalibre.saxo.util.DateExtensions.ExtendedLocalDateTime
import org.slf4j.LoggerFactory

import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

case class OpenApiCallingContext(sessionId : String, token : String) {

}

object OpenApiCallingContext {
  private val logger = LoggerFactory.getLogger(this.getClass)

  type OpenApiFunction[A] = (OpenApiService, OpenApiCallingContext) => Future[CallResult[A]]

  case class LockEntry(sessionId : String) {
    val lock = new {}
    var lastUsed = LocalDateTime.now()
  }
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

  def call[A](call : OpenApiFunction[A], sessionId : String)(implicit openApiService: OpenApiService, executionContext: ExecutionContext, sessionRepository: SessionRepository) : Future[CallResult[A]] =
    sessionRepository.liveSaxoToken(sessionId) match {
      case Some(token) => {
        implicit val context = OpenApiCallingContext(sessionId, token)
        callOn(call)
      }
      case None => sessionRepository.liveSaxoRefreshTokenFor(sessionId) match {
        case Some(refreshToken) => refreshAndCall(refreshToken, None, call,sessionId)
        case None => Future {
          Left(AuthorizationError)
        }
      }
    }

  def call[A](call : OpenApiFunction[A])(implicit openApiCallingContext: OpenApiCallingContext, openApiService: OpenApiService, executionContext: ExecutionContext, sessionRepository: SessionRepository) =
    callOn(call)


  private def callOn[A](call : OpenApiFunction[A])
                       (implicit context : OpenApiCallingContext, openApiService: OpenApiService, sessionRepository: SessionRepository, executionContext: ExecutionContext) : Future[CallResult[A]] = {
    val sessionId = context.sessionId
    val token = context.token
    var lockObjectOpt : Option[LockEntry] = None
    SessionLocksLock.synchronized {
      lockObjectOpt = SessionLocks.get(sessionId)
      if(lockObjectOpt.isEmpty) {
        val newLockObj = LockEntry(sessionId)
        SessionLocks(sessionId) = newLockObj
        lockObjectOpt = Some(newLockObj)
      }
    }
    lockObjectOpt.get.lock.synchronized {
      lockObjectOpt.get.lastUsed = LocalDateTime.now()
      call(openApiService, context).flatMap {
        case success @ Right(value) => Future{ success }
        case Left(ServiceResult.AuthorizationError) => {
          sessionRepository.liveSaxoRefreshTokenFor(context.token) match {
            case Some(refreshToken) => refreshAndCall(refreshToken,Some(token), call, sessionId)
            case _ => Future{ Left(ServiceResult.OtherError("Access token not valid and no live refresh token exists")) }
          }
        }
        case Left(err) => Future{ Left(err) }
      }
    }
  }

  private def refreshAndCall[A](refreshToken : String, currentToken : Option[String], call : OpenApiFunction[A], sessionId : String)
                               (implicit openApiService: OpenApiService, sessionRepository: SessionRepository, executionContext: ExecutionContext) : Future[CallResult[A]] =
    openApiService.refreshToken(refreshToken).flatMap {
    case Right(tokenReply) => {
      currentToken match {
        case Some(oldToken) => sessionRepository.updateSaxoTokenDataByCurrentToken(sessionId, oldToken, tokenReply.accessToken, tokenReply.expiresAt, tokenReply.refreshToken, tokenReply.refreshExpiresAt)
        case None => sessionRepository.updateLatestSaxoTokenDataBySessionId(sessionId,tokenReply.accessToken, tokenReply.expiresAt, tokenReply.refreshToken, tokenReply.refreshExpiresAt)
      }
      implicit val context = OpenApiCallingContext(sessionId, tokenReply.accessToken)
      callOn(call)
    }
    case Left(err) => Future { Left(err) }
  }





}
