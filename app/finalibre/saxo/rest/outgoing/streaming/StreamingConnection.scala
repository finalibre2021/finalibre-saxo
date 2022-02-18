package finalibre.saxo.rest.outgoing.streaming

import StreamingMessage.PayloadType
import StreamingMessage.PayloadType._
import akka.Done
import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers
import akka.http.scaladsl.model.headers.GenericHttpCredentials
import akka.http.scaladsl.model.ws.{Message, WebSocketRequest, WebSocketUpgradeResponse}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic
import io.circe._
import io.circe.parser._
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

class StreamingConnection private[StreamingConnection](
                         initialToken : String,
                         val contextId : String
                         )(implicit actorSystem: ActorSystem) {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private var token = initialToken
  private var currentState : Option[Json] = None
  private val currentBytes : ArrayBuffer[Byte] = ArrayBuffer.empty[Byte]
  private val activeSubscriptions : mutable.HashMap[String, StreamingSubscription[_]] = mutable.HashMap.empty
  private implicit val executionContext = actorSystem.dispatcher
  private val baseUrl = SaxoConfig.Rest.Outgoing.Streaming.streamingBaseUrl
  private var closePromise : Option[Promise[Option[Message]]] = None


  def updateToken(newToken : String) : Boolean = {
    token = newToken
    false
  }



  private[streaming] def openConnection() = {
    StreamingConnection.register(this)
    val endProm = createAkkaHttpWebSocketClientFlow()
    closePromise = Some(endProm)
  }

  private[streaming] def shutdownConnection() = {
    closePromise.foreach {
      case promise => promise.success(None)
    }
    StreamingConnection.unRegister(contextId)
  }

  private def handleBytes(bytes : Array[Byte]) : Unit = currentBytes.synchronized {
    currentBytes.appendAll(bytes)
    parse(currentBytes.toArray) match {
      case Left(err) => logger.error("During parsing of websocket message", err)
      case Right((mess, restBytes)) => {
        activeSubscriptions.get(mess.referenceId) match {
          case Some(sub) => sub.onMessage(mess)
        }
        currentBytes.clear()
        if(!restBytes.isEmpty)
          currentBytes.appendAll(restBytes)
      }
    }
  }

  private def createAkkaHttpWebSocketClientFlow() : Promise[Option[Message]] = {
    val sink = sinkFor()
    val flow : Flow[Message, Message, Promise[Option[Message]]] = Flow.fromSinkAndSourceMat(
      sink,
      Source.maybe[Message]
    )(Keep.right)

    val (upgradeResponse, endPromise) = Http().singleWebSocketRequest(
      WebSocketRequest(
        s"$baseUrl/connect?ContextId=$contextId",
        extraHeaders = Seq(
          headers.Authorization(GenericHttpCredentials("BEARER", token))
        )),
      flow
    )
    upgradeResponse.foreach {
      case upgrade => {
        logger.info(s"Succesfully established websocket connection for context ID: $contextId")
      }
    }
    endPromise
  }



  private def sinkFor() : Sink[Message, Future[Done]] =
    Sink.foreach {
      case mess => Try {
        val binary = mess.asBinaryMessage
        val byteString = binary.getStrictData
        val byteArray = byteString.toArray
        handleBytes(byteArray)
      } match {
        case Success(_) =>
        case Failure(err) => println(s"Error occured with sink: ${err.getMessage}")
      }
    }

  private def source()  =  Source.never

  private[streaming] def registerSubscription[A <: StreamingTopic](sub : StreamingSubscription[A]) : Unit = {
    activeSubscriptions.synchronized {
      activeSubscriptions(sub.referenceId) = sub
    }
  }

  private[streaming] def unRegisterSubscription(referenceId : String) : Unit = {
    activeSubscriptions.synchronized {
      activeSubscriptions.remove(referenceId)
    }
  }




  private def parse(input : Array[Byte]) : Either[Throwable,(StreamingMessage, Array[Byte])] = Try {
    val (reservedIndx, referenceSizeIndx) = (8, 10)
    val messageId = BigInt(input.take(reservedIndx)).toLong
    val bytesInReferenceId = input(referenceSizeIndx).toInt

    val (referenceIndx, payloadFormatIndx, payloadSizeIndx, payloadIndx) = (11, 11 + bytesInReferenceId, 12 + bytesInReferenceId, 16 + bytesInReferenceId)
    val referenceId = new String(input.slice(referenceIndx,payloadFormatIndx),"UTF-8")
    val payloadType = if(input(payloadFormatIndx).toInt == 0) PayloadType.Json else PayloadType.ProtoBuf
    val payloadSize = BigInt(input.slice(payloadSizeIndx, payloadIndx)).toLong.toInt
    val endIndex = payloadIndx + payloadSize
    val payload = new String(input.slice(payloadIndx, endIndex), "UTF-8")
    (StreamingMessage(messageId, referenceId, payloadType, payload), input.drop(endIndex))
  } match {
    case Failure(err) => Left(err)
    case Success(res) => Right(res)
  }
}

object StreamingConnection {
  private val connections = mutable.HashMap.empty[String, StreamingConnection]
  private[StreamingConnection] def register(connection : StreamingConnection) = connections.synchronized {
    connections(connection.contextId) = connection
  }

  private[StreamingConnection] def unRegister(contextId : String) : Unit = connections.synchronized {
    connections.remove(contextId)
  }

}



