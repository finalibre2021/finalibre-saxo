package finalibre.saxo.rest.outgoing.streaming

import akka.actor.ActorRef
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic
import io.circe.{Decoder, DecodingFailure, Json}
import io.circe.generic.auto._
import io.circe.generic.extras.Configuration
import io.circe.parser._
import org.slf4j.LoggerFactory

case class StreamingSubscription[A <: StreamingTopic] private (
                                referenceId : String,
                                initialState : Json,
                                firstObserver : StreamingObserver[A],
                                connection : StreamingConnection
                                ) {
  private val logger = LoggerFactory.getLogger(this.getClass)
  private var jsonState : Json = initialState
  private val listeners = scala.collection.mutable.HashMap.empty[Long, StreamingObserver[A]]
  listeners(firstObserver.observerId) = firstObserver

  private[streaming] def onMessage(mess : StreamingMessage) : Unit = {
     parse(mess.payload) match {
       case Left(parsingError) => logger.error(s"During parsing of payload: ${mess.payload}: ", parsingError)
       case Right(js) => {
         jsonState = jsonState.deepMerge(js)
         sendUpdate()
       }
     }
  }

  private[streaming] def registerObserver(observer : StreamingObserver[A]) = listeners.synchronized {
    listeners(observer.observerId) = observer
  }

  private[streaming] def unRegisterObserver(observerId : Long) = listeners.synchronized {
    listeners.remove(observerId)
    if(listeners.isEmpty){
      connection.unRegisterSubscription(referenceId)
    }
  }

  private def sendUpdate() : Unit = {
    jsonState.as[Seq[A]] match {
      case Right(res) => listeners.values.foreach(lis => lis.onSequenceUpdate(res))
      case _ => jsonState.as[A] match {
        case Right(res) => listeners.values.foreach(lis => lis.onUpdate(res))
        case Left(DecodingFailure(str, value)) => logger.error(s"Error during converting json: String: ${str}, value: ${value}")
      }
    }
  }

}
