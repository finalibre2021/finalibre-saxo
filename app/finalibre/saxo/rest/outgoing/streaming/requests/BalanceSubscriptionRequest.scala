package finalibre.saxo.rest.outgoing.streaming.requests

case class BalanceSubscriptionRequest(
                                       accountGroupKey : Option[String],
                                       accountKey : Option[String],
                                       clientKey : String,
                                       fieldGroups : Option[Seq[String]]
                                     ) extends SubscriptionRequest
