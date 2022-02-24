package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class SubPositionView(
                            ask : Double,
                            bid : Double,
                            calculationReliability : Option[String],
                            conversionRateClose : Double,
                            conversionRateCurrent : Double,
                            conversionRateOpen : Double,
                            currentPrice : Double,
                            currentPriceDelayMinutes : Int,
                            currentPriceLastTraded : LocalDateTime,
                            currentPriceType : String,
                            exposure : Double,
                            exposureCurrency : String,
                            exposureInBaseCurrency : Double,
                            indexRatio : Option[Double],
                            instrumentPriceDayPercentChange : Option[Double],
                            marketState : Option[String],
                            marketValue : Double,
                            openInterest : Option[Double],
                            profitLossCurrencyConversion : Double,
                            profitLossOnTrade : Double,
                            profitLossOnTradeInBaseCurrency : Double,
                            tradeCostsTotal : Double,
                            tradeCostsTotalInBaseCurrency : Double

                          )

