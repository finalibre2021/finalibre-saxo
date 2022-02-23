package finalibre.saxo.rest.outgoing.streaming.requests

case class AccountSubscriptionRequest(
                                     accountGroupKey : Option[String],
                                     accountKey : Option[String],
                                     clientKey : String
                                     ) extends SubscriptionRequest
