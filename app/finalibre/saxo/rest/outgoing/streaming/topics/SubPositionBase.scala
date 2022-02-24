package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.{LocalDate, LocalDateTime}

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

