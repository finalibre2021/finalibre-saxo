package finalibre.saxo.rest.outgoing.streaming.requests

case class MultiLegSubscriptionRequest(
                                      accountKey : Option[String],
                                      fieldGroups : Option[Seq[String]],
                                      legs : Seq[MultiLegStrategyLegRequest]
                                      ) extends SubscriptionRequest


case class MultiLegStrategyLegRequest(
                                     amount : Option[Double],
                                     assetType : Option[String],
                                     buySell : Option[String],
                                     orderContext : Option[String],
                                     toOpenClose : Option[String],
                                     uic : Long
                                     )