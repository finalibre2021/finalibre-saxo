package finalibre.saxo.rest.outgoing.streaming.topics
//import play.api.libs.functional.syntax.{__, toFunctionalBuilderOps}
import play.api.libs.functional.syntax._
import play.api.libs.json._

import play.api.libs.json.OFormat

import java.time.LocalDateTime

object ManualReads {
  object AccountEntryFormat {
    val partial1Reads: OFormat[(String, String, String, String, Boolean, Boolean, Boolean, String, String, LocalDateTime, String, Int, Boolean, Boolean,
      Boolean, Boolean, Boolean, Boolean, Boolean, Seq[String])] = (
      (__ \ "AccountGroupKey").format[String]
        ~ (__ \ "AccountId").format[String]
        ~ (__ \ "AccountKey").format[String]
        ~ (__ \ "AccountType").format[String]
        ~ (__ \ "Active").format[Boolean]
        ~ (__ \ "CanUseCashPositionsAsMarginCollateral").format[Boolean]
        ~ (__ \ "CfdBorrowingCostsActive").format[Boolean]
        ~ (__ \ "ClientId").format[String]
        ~ (__ \ "ClientKey").format[String]
        ~ (__ \ "CreationDate").format[LocalDateTime]
        ~ (__ \ "Currency").format[String]
        ~ (__ \ "CurrencyDecimals").format[Int]
        ~ (__ \ "DirectMarketAccess").format[Boolean]
        ~ (__ \ "FractionalOrderEnabled").format[Boolean]
        ~ (__ \ "IndividualMargining").format[Boolean]
        ~ (__ \ "IsCurrencyConversionAtSettlementTime").format[Boolean]
        ~ (__ \ "IsMarginTradingAllowed").format[Boolean]
        ~ (__ \ "IsShareable").format[Boolean]
        ~ (__ \ "IsTrialAccount").format[Boolean]
        ~ (__ \ "LegalAssetType").format[Seq[String]]

      ).tupled

    val partial2Reads: OFormat[(String, String, String, Seq[String], Boolean, Boolean)] = (
      (__ \ "managementType").format[String]
        ~ (__ \ "marginCalculationMethod").format[String]
        ~ (__ \ "marginLendingEnabled").format[String]
        ~ (__ \ "sharing").format[Seq[String]]
        ~ (__ \ "supportsAccountValueProtectionLimit").format[Boolean]
        ~ (__ \ "useCashPositionsAsMarginCollateral").format[Boolean]
      ).tupled

    val combined = (partial1Reads ~ partial2Reads).tupled
     val reads  : Reads[AccountEntry] = combined.map {
      case ((accountGroupKey,accountId,accountKey,accountType,active, canUseCashPositionsAsMarginCollateral,cfdBorrowingCostsActive,clientId,
      clientKey, creationDate, currency, currencyDecimals, directMarketAccess, fractionalOrderEnabled, individualMargining, isCurrencyConversionAtSettlementTime,
      isMarginTradingAllowed, isShareable, isTrialAccount, legalAssetType),
      (managementType, marginCalculationMethod, marginLendingEnabled, sharing, supportsAccountValueProtectionLimit, useCashPositionsAsMarginCollateral)) =>
        AccountEntry(accountGroupKey, accountId,accountKey,accountType,active, canUseCashPositionsAsMarginCollateral,cfdBorrowingCostsActive,clientId,
          clientKey, creationDate, currency, currencyDecimals, directMarketAccess, fractionalOrderEnabled, individualMargining, isCurrencyConversionAtSettlementTime,
          isMarginTradingAllowed, isShareable, isTrialAccount, legalAssetType, managementType, marginCalculationMethod, marginLendingEnabled, sharing,
          supportsAccountValueProtectionLimit, useCashPositionsAsMarginCollateral)
    }

  }

  object SubPositionViewRead {
    case class SubPositionPartial1(ask:Double, bid:Double, calculationReliability:Option[String], conversionRateClose:Double, conversionRateCurrent:Double, conversionRateOpen:Double, currentPrice:Double, currentPriceDelayMinutes:Int, currentPriceLastTraded:LocalDateTime, currentPriceType:String, exposure:Double, exposureCurrency:String, exposureInBaseCurrency:Double, indexRatio:Option[Double], instrumentPriceDayPercentChange:Option[Double], marketState:Option[String], marketValue:Double, openInterest:Option[Double], profitLossCurrencyConversion:Double, profitLossOnTrade:Double)
    case class SubPositionPartial2(profitLossOnTradeInBaseCurrency : Double,tradeCostsTotal : Double, tradeCostsTotalInBaseCurrency : Double)

    val part1Reads = Json.reads[SubPositionPartial1]
    val part2reads = Json.reads[SubPositionPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
      SubPositionPartial1(ask ,bid ,calculationReliability ,conversionRateClose ,conversionRateCurrent ,conversionRateOpen ,currentPrice ,
      currentPriceDelayMinutes ,currentPriceLastTraded ,currentPriceType ,exposure ,exposureCurrency ,exposureInBaseCurrency ,
      indexRatio ,instrumentPriceDayPercentChange ,marketState ,marketValue ,openInterest ,profitLossCurrencyConversion ,profitLossOnTrade),
      SubPositionPartial2(profitLossOnTradeInBaseCurrency,tradeCostsTotal, tradeCostsTotalInBaseCurrency)
      ) => SubPositionView(
        ask ,bid ,calculationReliability ,conversionRateClose ,conversionRateCurrent ,conversionRateOpen ,currentPrice ,
        currentPriceDelayMinutes ,currentPriceLastTraded ,currentPriceType ,exposure ,exposureCurrency ,exposureInBaseCurrency ,
        indexRatio ,instrumentPriceDayPercentChange ,marketState ,marketValue ,openInterest ,profitLossCurrencyConversion ,profitLossOnTrade,
        profitLossOnTradeInBaseCurrency,tradeCostsTotal, tradeCostsTotalInBaseCurrency
      )
    }






  }




}
