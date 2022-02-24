package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

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

