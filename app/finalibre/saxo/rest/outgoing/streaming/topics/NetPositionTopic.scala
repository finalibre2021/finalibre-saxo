package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.{LocalDate, LocalDateTime}

case class NetPositionTopic(
                           displayAndFormat: DisplayAndFormat,
                           exchange: InstrumentExchangeDetails,
                           greeks : Option[GreeksDetails],
                           netPositionBase : NetPositionStatic,
                           netPositionId : String,
                           netPositionView : NetPositionDynamic,
                           singlePosition : Option[SubPositionResponse]
                           ) extends StreamingTopic

case class NetPositionStatic(
                              accountId : Option[String],
                              amount : Double,
                              amountLong : Option[Double],
                              amountShort : Option[Double],
                              assetType: String,
                              blockedQuantity : Double,
                              canBeClosed : Boolean,
                              clientId : String,
                              expiryDate : Option[LocalDateTime],
                              fixedIncomeData : Option[FixedIncomeData],
                              hasForceOpenPositions : Option[Boolean],
                              isMarketOpen : Boolean,
                              marketState : String,
                              nonTradableReason : Option[String],
                              noticeDate : Option[LocalDateTime],
                              numberOfRelatedOrders : Int,
                              openIndexRatioAverage : Option[Double],
                              openIpoOrdersCount : Option[Int],
                              openOrdersCount : Option[Int],
                              openTriggerOrdersCount : Option[Int],
                              optionsData : Option[OptionsData],
                              positionsAccount : Option[String],
                              shortTrading : Option[String],
                              singlePositionAccountId : Option[String],
                              singlePositionId : Option[String],
                              singlePositionStatus : Option[String],
                              srdLastTradeDate : Option[LocalDateTime],
                              srdSettlementDate : Option[LocalDateTime],
                              tradingStatus : String,
                              uic : Long,
                              valueDate : LocalDateTime
                            )


case class FixedIncomeData(
                            closedAccruedInterest : Double,
                            closedAccruedInterestInBaseCurrency : Double,
                            openAccruedInterest : Double,
                            openAccruedInterestInBaseCurrency : Double
                          )

case class NetPositionDynamic(
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
                             )





case class SubPositionResponse(
                              positionId : String,
                              positionBase : SubPositionBase,
                              positionView : SubPositionView
                              )

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


case class SubPositionBase(
                            accountId : String,
                            accountKey : String,
                            amount : Double,
                            assetType: String,
                            blockedQuantity : Double,
                            canBeClosed : Boolean,
                            clientId : String,
                            closeConversionRateSettled : Boolean,
                            contractId : Option[Long],
                            correlationKey : Option[String],
                            correlationTypes : Option[Seq[String]],
                            executionTimeClose : Option[LocalDateTime],
                            executionTimeOpen : Option[LocalDateTime],
                            expiryDate : Option[LocalDateTime],
                            externalReference : Option[String],
                            fixedIncomeData : Option[FixedIncomeData],
                            isForceOpen : Option[Boolean],
                            isMarketOpen : Boolean,
                            noticeDate : Option[LocalDateTime],
                            openIndexRatio : Option[Double],
                            openPrice : Double,
                            openPriceIncludingCosts : Double,
                            optionsData : Option[OptionsData],
                            relatedOpenOrders : Option[Seq[RelatedOrderInfo]],
                            relatedPositionId : Option[String],
                            sourceOrderId : String,
                            spotDate : LocalDate,
                            srdLastTradeDate : Option[LocalDateTime],
                            srdSettlementDate : Option[LocalDateTime],
                            status : String,
                            toOpenClose : Option[String],
                            uic : Long,
                            valueDate : LocalDateTime
                          )

