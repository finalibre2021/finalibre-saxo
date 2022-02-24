package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class PositionDynamic(
                            ask : Double,
                            averageOpenPrice : Double,
                            bid : Double,
                            calculationReliability : Option[String],
                            conversionRateCurrent : Double,
                            currentPrice : Double,
                            currentPriceDelayMinutes : Int,
                            currentPriceLastTraded : LocalDateTime,
                            currentPriceType : String,
                            exposure : Double,
                            exposureCurrency : String,
                            exposureInBaseCurrency : Double,
                            indexRatio : Option[Double],
                            instrumentPriceDayPercentChange : Option[Double],
                            marketValue : Double,
                            marketValueInBaseCurrency : Double,
                            marketValueOpen : Double,
                            marketValueOpenInBaseCurrency : Double,
                            openInterest : Option[Double],
                            positionCount : Int,
                            positionsNotClosedCount : Int,
                            profitLossCurrencyConversion : Double,
                            profitLossOnTrade : Double,
                            profitLossOnTradeInBaseCurrency : Double,
                            realizedCostsTotal : Double,
                            realizedCostsTotalInBaseCurrency : Double,
                            realizedProfitLoss : Double,
                            realizedProfitLossInBaseCurrency : Double,
                            settlementInstructions : Option[SettlementInstructions],
                            status : String,
                            tradeCostsTotal : Double,
                            tradeCostsTotalInBaseCurrency : Double
                          ) extends StreamingTopic





