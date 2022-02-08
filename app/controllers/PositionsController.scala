package controllers

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.stream.Materializer
import finalibre.saxo.client.positions.messages.{ToClientMessage, ToServerMessage}
import finalibre.saxo.client.positions.model.{ClientDto, PositionDto}
import finalibre.saxo.positions.RestPositionDataLoader
import finalibre.saxo.positions.mappers.PositionMappers.PositionMapper
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.security.{Encryptor, SessionRepository}
import finalibre.saxo.util.Pingable
import org.slf4j.LoggerFactory
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PositionsController @Inject()(
                                     cc : ControllerComponents,
                                     executionContext : ExecutionContext,
                                     sessionRepository: SessionRepository,
                                     encryptor : Encryptor,
                                     openApiService: OpenApiService
                                   )(implicit inSys : ActorSystem, inMat : Materializer) extends FinaLibreController(cc, executionContext, sessionRepository, encryptor, openApiService){

  private val logger = LoggerFactory.getLogger(this.getClass)
  implicit val writesClient : Writes[ClientDto] = Json.writes[ClientDto]
  implicit val writesPosition : Writes[PositionDto] = Json.writes[PositionDto]
  implicit val writesMessage : Writes[ToClientMessage] = Json.writes[ToClientMessage]
  implicit val readsMessage : Reads[ToServerMessage] = Json.reads[ToServerMessage]


  def positionsIndex() : Action[AnyContent] = actionFrom {
    case (request : Request[AnyContent], context) => Future {
      Ok(views.html.positions())
    }
  }

  class PositionsWSActor(out : ActorRef, sessionId : String) extends Actor with Pingable {
    val positionsDataLoader = RestPositionDataLoader(sessionId)

    def send(message : ToClientMessage) : Unit = {
      val asJson = Json.toJson(message)
      val asString = Json.stringify(asJson)
      out ! asString
    }

    override def receive: Receive = {
      case message => FinaLibreController.wrapAction(logger)(message) {
        case _ => {
          val parsed = Json.parse(message.toString).as[ToServerMessage]
          parsed.messageType match {
            case ToServerMessage.SelectClientMessageType => parsed.clientKey.foreach {
              case clientKey => positionsDataLoader.loadPositions(clientKey).foreach {
                case res => res match {
                  case Left(err) => logger.error(s"Failed to load positions data. Error: $err")
                  case Right(poss) => send(ToClientMessage(positions = poss.map(_.toDto)))
                }
              }
            }
          }
        }
      }
    }

    override def pingOut: ActorRef = out
  }

}

