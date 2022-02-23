package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class TradeMessageTopic(
                            dateTime : Option[LocalDateTime],
                            displayType : Option[String],
                            messageBody : String,
                            messageHeader : String,
                            messageId : String,
                            messageType : String
                            ) extends StreamingTopic
