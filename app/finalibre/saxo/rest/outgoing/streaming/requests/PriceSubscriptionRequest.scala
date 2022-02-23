package finalibre.saxo.rest.outgoing.streaming.requests

import java.time.LocalDateTime

case class PriceSubscriptionRequest(
                                     accountKey : Option[String],
                                     amount : Option[Double],
                                     amountType : Option[String],
                                     assetType : String,
                                     contractId : Option[Long],
                                     expiryDate : Option[LocalDateTime],
                                     fieldGroups : Option[Seq[String]],
                                     forwardDate : Option[LocalDateTime],
                                     forwardDateFarLeg : Option[LocalDateTime],
                                     forwardDateNearLeg : Option[LocalDateTime],
                                     lowerBarrier : Option[Double],
                                     orderAskPrice : Option[Double],
                                     orderBidPrice : Option[Double],
                                     putCall : Option[String],
                                     quoteCurrency : Option[Boolean],
                                     requireTradableQuote : Option[Boolean],
                                     sharePrice : Option[Double],
                                     strategyName : Option[String],
                                     strikePrice : Option[Double],
                                     toClosePositionId : Option[Long],
                                     toOpenClose : Option[String],
                                     uic : Long,
                                     upperBarrier : Option[Double]
                                   ) extends SubscriptionRequest

