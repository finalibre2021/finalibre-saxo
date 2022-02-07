package controllers

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.stream.Materializer
import controllers.FinaLibreController.FinaLibreControllerContext
import finalibre.saxo.client.positions.messages.ToServerMessage
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.security.{Encryptor, SessionRepository}
import finalibre.saxo.util.Pingable
import org.slf4j.LoggerFactory
import play.api.libs.json.{Json, Reads}
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

  private val logger = LoggerFactory.getLogger(this.getClass)
  private implicit val execContx = executionContext


  def positionsIndex() : Action[AnyContent] = actionFrom {
    case (request : Request[AnyContent], context) => {
      implicit val contx = context
      callOn(serv => serv.defaultClient()(context.token)).map {
        case Left(err) => Ok(err.toString)
        case Right(client) => Ok(views.html.positions())
      }
    }
  }

  class PositionsWSActor(out : ActorRef)(implicit executionContext: ExecutionContext) extends Actor with Pingable {
    import PositionsController.readsMessage
    override def receive: Receive = {
      case message => FinaLibreController.wrapAction(logger)(message) {
        case _ => Json.parse(message.toString).as[ToServerMessage] match {

        }
      }
    }

    override def pingOut: ActorRef = out
  }

}

object PositionsController {
  implicit val readsMessage : Reads[ToServerMessage] = Json.reads[ToServerMessage]
}