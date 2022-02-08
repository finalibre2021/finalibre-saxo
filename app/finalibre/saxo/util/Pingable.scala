package finalibre.saxo.util

import akka.actor.{Actor, ActorRef}
import finalibre.saxo.util.Pingable.{Ping, registerPingable}
import play.api.libs.json.Json

trait Pingable {
  this : Actor =>
  implicit val pingFormat = Json.writes[Ping.type]

  val pingableId = Pingable.nextId
  def pingOut : ActorRef

  override def postStop(): Unit = {
    Pingable.unRegisterPingable(pingableId)
  }

  def ping = {
    pingOut ! Json.toJson(Pingable.pingMessage)
  }

  registerPingable(this)
}

object Pingable {
  object Ping
  val pingMessage = Ping
  private var id = 0L
  def nextId = Pingable.synchronized {
    id += 1
    id
  }
  private val pingables = scala.collection.mutable.HashMap.empty[Long,Pingable]

  def registerPingable(pingable : Pingable) =  {
    pingables(pingable.pingableId) = pingable
  }

  def unRegisterPingable(pingableId : Long) = {
    pingables -= pingableId
  }

  def ping() = {
    pingables.foreach(_._2.ping)

  }

}