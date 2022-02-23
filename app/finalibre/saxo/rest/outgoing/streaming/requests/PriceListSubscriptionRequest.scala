package finalibre.saxo.rest.outgoing.streaming.requests

import java.time.LocalDateTime

case class PriceListSubscriptionRequest(
                                       accountKey : Option[String],
                                       amount : Option[Double],
                                       amountType : Option[String],
                                       assetType : String,
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
                                       strikePrice : Option[Double],
                                       toOpenClose : Option[String],
                                       uics : String,
                                       upperBarrier : Option[Double]
                                       ) extends SubscriptionRequest

