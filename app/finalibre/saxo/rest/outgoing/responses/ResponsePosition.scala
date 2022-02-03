package finalibre.saxo.rest.outgoing.responses

import finalibre.saxo.rest.outgoing.Encoding
import play.api.libs.json.{Json, Reads}

import java.time.{LocalDate, LocalDateTime}

case class ResponsePosition(
                           netPositionId : String,
                           positionId : String,
                           positionBase : PositionBase,
                           positionView: PositionView
                           )

case class PositionBase(
                         accountId : String,
                         amount : Double,
                         assetType: String,
                         canBeClosed : Boolean,
                         clientId : String,
                         closeConversionRateSettled : Boolean,
                         executionTimeOpen: LocalDateTime,
                         isForceOpen : Boolean,
                         isMarketOpen : Boolean,
                         openPrice: Double,
                         spotDate: LocalDate,
                         status: String,
                         uic: Int,
                         valueDate: LocalDateTime
                       )

case class PositionView(
                         ask: Double,
                         bid: Double,
                         calculationReliability: String,
                         currentPrice: Double,
                         currentPriceDelayMinutes: Long,
                         currentPriceType: String,
                         exposure: Double,
                         exposureCurrency: String,
                         exposureInBaseCurrency: Double,
                         instrumentPriceDayPercentChange: Double,
                         profitLossOnTrade: Double,
                         profitLossOnTradeInBaseCurrency: Double,
                         tradeCostsTotal: Double,
                         tradeCostsTotalInBaseCurrency: Double,
                         settlementInstruction: Option[SettlementInstruction]
                       )

case class SettlementInstruction(
                                  actualRolloverAmount: Double,
                                  actualSettlementAmount: Double,
                                  amount: Double,
                                  isSettlementInstructionsAllowed: Boolean,
                                  month: Long,
                                  settlementType: String,
                                  year: Long
                                )

object ResponsePosition {
  implicit val config = Encoding.DefaultConfiguration
  private implicit val positionBaseReads : Reads[PositionBase] = Json.reads[PositionBase]
  private implicit val settlementInstructionReads : Reads[SettlementInstruction] = Json.reads[SettlementInstruction]
  private implicit val positionViewReads : Reads[PositionView] = Json.reads[PositionView]
  private implicit val responsePositionReads : Reads[ResponsePosition] = Json.reads[ResponsePosition]
  private implicit val dataReads : Reads[DataObject[ResponsePosition]] = Json.reads[DataObject[ResponsePosition]]
  implicit val reads : Reads[List[ResponsePosition]] = dataReads.map(accDat => accDat.data.toList)
}
