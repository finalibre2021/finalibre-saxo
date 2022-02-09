package finalibre.saxo.client.positions.messages

case class ToServerMessage(
                          messageType : String,
                          clientKeys : Option[Seq[String]] = None
                          )

object ToServerMessage {
  val RequestInitialDataMessageType = "InitialData"
  val SelectClientsMessageType = "SelectClients"
}
