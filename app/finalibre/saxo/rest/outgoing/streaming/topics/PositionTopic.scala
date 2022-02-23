package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.{LocalDate, LocalDateTime}

case class PositionTopic(
                           displayAndFormat: DisplayAndFormat,
                           exchange: InstrumentExchangeDetails,
                           greeks : Option[GreeksDetails],
                           netPositionId : String,
                           positionBase : PositionStatic,
                           positionId : String,
                           positionView : PositionDynamic
                           ) extends StreamingTopic

case class PositionStatic(
                              accountId : String,
                              accountKey : String,
                              amount : Double,
                              assetType: String,
                              canBeClosed : Boolean,
                              clientId : String,
                              closeConversionRateSettled : Option[Boolean],
                              contractId : Option[Long],
                              correlationKey : Option[String],
                              correlationTypes : Option[Seq[String]],
                              executionTimeClose : Option[LocalDateTime],
                              executionTimeOpen : Option[LocalDateTime],
                              expiryDate : Option[LocalDateTime],
                              fixedIncomeData : Option[FixedIncomeData],
                              isForceOpen : Option[Boolean],
                              isMarketOpen : Boolean,
                              marketState : String,
                              noticeDate : Option[LocalDateTime],
                              openIndexRatio : Option[Double],
                              openPrice : Option[Double],
                              openPriceIncludingCosts : Option[Double],
                              optionsData : Option[OptionsData],
                              relatedOptionOrders : Option[Seq[RelatedOrderInfo]],
                              sourceOrderId : Option[String],
                              spotDate : Option[LocalDateTime],
                              srdLastTradeDate : Option[LocalDateTime],
                              srdSettlementDate : Option[LocalDateTime],
                              status : String,
                              toOpenClose : Option[String],
                              uic : Long,
                              valueDate : LocalDateTime
                            )



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





