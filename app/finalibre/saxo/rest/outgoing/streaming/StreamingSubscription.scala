package finalibre.saxo.rest.outgoing.streaming

import akka.actor.ActorRef
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic
import io.circe.{Decoder, Json}
import io.circe.parser._
import org.slf4j.LoggerFactory

case class StreamingSubscription[A <: StreamingTopic] private (
                                referenceId : String,
                                initialState : Json,
                                actorRef : ActorRef
                                )(implicit decode : Decoder[A]) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private var jsonState : Json = initialState

  private[streaming] def onMessage(mess : StreamingMessage) : Unit = {
     parse(mess.payload) match {
       case Left(parsingError) => logger.error(s"During parsing of payload: ${mess.payload}: ", parsingError)
       case Right(js) => jsonState = jsonState.deepMerge(js)
     }
  }

  def currentState : Either[String,A] = {
    jsonState.as[A] match {
      case Left(err) => Left(err.message)
      case Right(res) => Right(res)
    }
  }

}