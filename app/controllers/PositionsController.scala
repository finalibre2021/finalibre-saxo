package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.security.{Encryptor, SessionRepository}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PositionsController @Inject()(
                                     cc : ControllerComponents,
                                     executionContext : ExecutionContext,
                                     sessionRepository: SessionRepository,
                                     encryptor : Encryptor,
                                     openApiService: OpenApiService
                                   )(implicit inSys : ActorSystem, inMat : Materializer) extends FinaLibreController(cc, executionContext, sessionRepository, encryptor, openApiService){

  def positionsIndex() : Action[AnyContent] = Action {
    implicit request : Request[AnyContent] => Ok(views.html.main())
  }

}
