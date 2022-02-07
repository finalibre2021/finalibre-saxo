package finalibre.saxo.system
import akka.actor.ActorSystem
import controllers.FinaLibreController
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.security.SessionRepository
import finalibre.saxo.util.Pingable

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

@Singleton()
class SaxoScheduler  @Inject()(actorSystem : ActorSystem, sessionRepository: SessionRepository, openApiService : OpenApiService)(implicit executionContext: ExecutionContext) {
  actorSystem.scheduler.scheduleAtFixedRate(
    SaxoConfig.tokenRefreshIntervalInMinutes.minutes,
    SaxoConfig.tokenRefreshIntervalInMinutes.minutes
  )(() => {
    FinaLibreController.cleanUpUnusedSessionLocks()
    FinaLibreController.refreshRelevantConnections(sessionRepository, openApiService)
  })
  actorSystem.scheduler.scheduleAtFixedRate(25.seconds,25.seconds)(() => Pingable.ping())
}
