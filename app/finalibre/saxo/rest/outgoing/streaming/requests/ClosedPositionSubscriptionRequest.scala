package finalibre.saxo.rest.outgoing.streaming.requests

case class ClosedPositionSubscriptionRequest(
                                              accountGroupKey : Option[String],
                                              accountKey : Option[String],
                                              clientKey : String,
                                              closedPositionId : Option[String],
                                              fieldGroups : Option[Seq[String]]
                                            ) extends SubscriptionRequest
