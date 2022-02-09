package controllers

import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.stream.Materializer
import finalibre.saxo.client.positions.messages.{ToClientMessage, ToServerMessage}
import finalibre.saxo.client.positions.model.{ClientDto, PositionDto}
import finalibre.saxo.positions.RestPositionDataLoader
import finalibre.saxo.positions.mappers.ClientMappers.ClientMapper
import finalibre.saxo.positions.mappers.PositionMappers.PositionMapper
import finalibre.saxo.rest.outgoing.{OpenApiCallingContext, OpenApiService}
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

  def socket = wsFrom((out, cont) => new PositionsWSActor(out, cont.sessionId) )


  class PositionsWSActor(out : ActorRef, sessionId : String) extends FinaLibreWSActor(out, sessionId) {
    val positionsDataLoader = RestPositionDataLoader(sessionId)

    def send(message : ToClientMessage) : Unit = {
      val asJson = Json.toJson(message)
      out ! asJson
    }

    override def receive: Receive = {
      case message => FinaLibreController.wrapAction(logger)(message) {
        case _ => {
          val parsed = Json.parse(message.toString).as[ToServerMessage]
          parsed.messageType match {
            case ToServerMessage.RequestInitialDataMessageType => {
              positionsDataLoader.loadClients().foreach {
                case Left(err) => {
                  logger.error(s"Error when loading clients: $err")
                }
                case Right(clients) => {
                  send(ToClientMessage(clients = Some(clients.map(_.toDto))))
                }
              }
            }
            case ToServerMessage.SelectClientsMessageType => parsed.clientKeys.foreach {
              case clientKeys => {
                val futures = clientKeys.map(cli => positionsDataLoader.loadPositions(cli))
                val res = futures.foldLeft(Future {None.asInstanceOf[Option[Seq[PositionDto]]] }) {
                  case (current, next) => next.flatMap {
                    case Left(err) => {
                      logger.error(s"Failed to load positions data. Error: $err")
                      current
                    }
                    case Right(poss) => {
                      current.map(curr => Some((curr.getOrElse(Nil) ++ poss.map(_.toDto)).toList))
                    }
                  }
                }
                for(
                  futureRes <- res;
                  poss <- futureRes
                ) send(ToClientMessage(positions = Some(poss)))

              }
            }
          }
        }
      }
    }

    override def pingOut: ActorRef = out
  }

}

