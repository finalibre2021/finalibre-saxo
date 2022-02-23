package finalibre.saxo.rest.outgoing.streaming.topics

case class SessionStateTopic(
                            authenticationLevel : String,
                            dataLevel : String,
                            tradeLevel : String
                            ) extends StreamingTopic
