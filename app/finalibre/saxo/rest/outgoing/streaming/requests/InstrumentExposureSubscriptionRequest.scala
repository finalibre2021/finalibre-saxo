package finalibre.saxo.rest.outgoing.streaming.requests

import java.time.LocalDateTime

case class InstrumentExposureSubscriptionRequest(
                                                  accountGroupKey : Option[String],
                                                  accountKey : Option[String],
                                                  assetType : Option[String],
                                                  clientKey : String,
                                                  expiryDate : Option[LocalDateTime],
                                                  lowerBarrier : Option[Double],
                                                  putCall : Option[String],
                                                  strike : Option[Double],
                                                  upperBarrier : Option[Double],
                                                  valueDate : Option[LocalDateTime]
                                                ) extends SubscriptionRequest
