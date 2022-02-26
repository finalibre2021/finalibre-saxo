package finalibre.saxo.rest.outgoing.streaming.requests

case class ChartSubscriptionRequest(
                                   assetType : String,
                                   count : Option[Int],
                                   fieldGroups : Option[Seq[String]],
                                   horizon : Int,
                                   uic : Long
                                   ) extends SubscriptionRequest
