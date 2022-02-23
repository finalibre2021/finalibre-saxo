package finalibre.saxo.rest.outgoing.streaming.requests

import java.time.LocalDateTime

case class OrdersSubscriptionRequest(
                                      accountGroupKey : Option[String],
                                      accountKey : Option[String],
                                      clientKey : String,
                                      fieldGroups : Option[Seq[String]],
                                      orderId : Option[String],
                                      status : Option[String],
                                      watchlistId : Option[String]
                                    ) extends SubscriptionRequest
