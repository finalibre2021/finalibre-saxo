package finalibre.saxo.rest.outgoing.streaming.requests

case class OptionChainRequest(
                             accountKey : Option[String],
                             amount : Option[Double],
                             assetType : String,
                             identifier : Long,
                             maxStrikesPerExpiry : Option[Int]
                             ) extends SubscriptionRequest

