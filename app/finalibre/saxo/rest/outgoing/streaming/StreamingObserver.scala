package finalibre.saxo.rest.outgoing.streaming

import akka.actor.{Actor, ActorRef}
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic

trait StreamingObserver[T <: StreamingTopic] {
  this : Actor =>

  private[streaming] val observerId = StreamingObserver.nextId()

  private var subscription : Option[StreamingSubscription[T]] = None

  override def postStop(): Unit = {
    subscription.foreach {
      case sub => {
        sub.unRegisterObserver(observerId)
      }
    }
  }

  def onUpdate(message : T) : Unit
  def onSequenceUpdate(messageSeq : Seq[T]) : Unit

  private[streaming] def registerSubscription(sub : StreamingSubscription[T]) : Unit = {
    subscription = Some(sub)
    sub.registerObserver(this)
  }
}

object StreamingObserver {
  private var currentId = 0L

  private[StreamingSubscription] def nextId() : Long = StreamingObserver.synchronized {
    currentId += 1
    currentId
  }

}
