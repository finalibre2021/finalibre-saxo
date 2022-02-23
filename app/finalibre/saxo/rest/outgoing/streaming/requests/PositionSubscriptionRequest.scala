package finalibre.saxo.rest.outgoing.streaming.requests

case class PositionSubscriptionRequest(
                                        accountGroupKey : Option[String],
                                        accountKey : Option[String],
                                        clientKey : String,
                                        fieldGroups : Option[Seq[String]],
                                        netPositionId : Option[String],
                                        positionId : Option[String],
                                        watchlistId : Option[String]
                                      ) extends SubscriptionRequest
