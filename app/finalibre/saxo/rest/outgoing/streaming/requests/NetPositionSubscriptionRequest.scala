package finalibre.saxo.rest.outgoing.streaming.requests

import java.time.LocalDateTime

case class NetPositionSubscriptionRequest(
                                           accountGroupKey : Option[String],
                                           accountKey : Option[String],
                                           assetType : Option[String],
                                           clientKey : String,
                                           expiryDate : Option[LocalDateTime],
                                           fieldGroups : Option[Seq[String]],
                                           lowerBarrier : Option[Double],
                                           netPositionId : Option[String],
                                           putCall : Option[String],
                                           strike : Option[Double],
                                           uic : Option[Long],
                                           upperBarrier : Option[Double],
                                           valueDate : Option[LocalDateTime],
                                           watchlistId : Option[String]
                                         ) extends SubscriptionRequest
