package finalibre.saxo.client.positions.messages

case class ToServerMessage(
                          messageType : String,
                          clientKey : Option[String]
                          )

object ToServerMessage {
  val SelectClientMessageType = "SelectClient"
}
