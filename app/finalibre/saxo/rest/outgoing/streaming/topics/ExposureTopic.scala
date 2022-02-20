package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ExposureTopic(
                          amount : Double,
                          assetType : String,
                          averageOpenPrice : Double,
                          calculationReliability : Option[String],
                          canBeClosed : Option[Boolean],
                          displayAndFormat: DisplayAndFormat,
                          expiryDate : Option[LocalDateTime],
                          instrumentPriceDayPercentChange : Double,
                          lowerBarrier : Option[Double],
                          netPositionId : Option[String],
                          profitLossOnTrade : Double,
                          putCall : Option[String],
                          strike : Option[Double],
                          uic : Int,
                          upperBarrier : Option[Double],
                          valueDate : Option[LocalDateTime]
                        ) extends StreamingTopic
