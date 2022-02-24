package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ClosedPositionTopic(
                              accountId : String,
                              amount : Double,
                              assetType : String,
                              buyOrSell : String,
                              clientId : String,
                              closedProfitLoss : Double,
                              closedProfitLossInBaseCurrency : Double,
                              closingIndexRatio : Double,
                              closingMarketValue : Double,
                              closingMarketValueInBaseCurrency : Double,
                              closingMethod : String,
                              closingPositionId : String,
                              closingPremium : Double,
                              closingPremiumInBaseCurrency : Double,
                              closingPrice : Double,
                              conversionRateInstrumentToBaseSettledClosing : Boolean,
                              conversionRateInstrumentToBaseSettledOpening : Boolean,
                              costClosing : Double,
                              costClosingInBaseCurrency : Double,
                              costOpening : Double,
                              costOpeningInBaseCurrency : Double,
                              executionTimeClose : LocalDateTime,
                              executionTimeOpen : LocalDateTime,
                              expiryDate : Option[LocalDateTime],
                              fxOptionData : Option[FxOptionsBaseData],
                              noticeDate : Option[LocalDateTime],
                              openingIndexRatio : Option[Double],
                              openingPositionId : String,
                              openPrice : Double,
                              profitLossCurrencyConversion : Double,
                              profitLossOnTrade : Double,
                              profitLossOnTradeInBaseCurrency : Double,
                              srdSettlementDate : LocalDateTime,
                              uic : Int
                              ) extends StreamingTopic

