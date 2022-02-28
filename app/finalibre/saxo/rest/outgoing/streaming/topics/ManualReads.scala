package finalibre.saxo.rest.outgoing.streaming.topics
//import play.api.libs.functional.syntax.{__, toFunctionalBuilderOps}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.OFormat

import java.time.{LocalDate, LocalDateTime}

object ManualReads {
  object AccountEntryReads {
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

  object SubPositionViewReads {
    case class SubPositionPartial1(ask:Double, bid:Double, calculationReliability:Option[String], conversionRateClose:Double, conversionRateCurrent:Double,
                                   conversionRateOpen:Double, currentPrice:Double, currentPriceDelayMinutes:Int, currentPriceLastTraded:LocalDateTime,
                                   currentPriceType:String, exposure:Double, exposureCurrency:String, exposureInBaseCurrency:Double, indexRatio:Option[Double],
                                   instrumentPriceDayPercentChange:Option[Double], marketState:Option[String], marketValue:Double, openInterest:Option[Double],
                                   profitLossCurrencyConversion:Double, profitLossOnTrade:Double)
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

  object InstrumentPriceDetailReads {
    case class InstrumentPriceDetailsPartial1(
                                       accruedInterest : Option[Double], askYield : Option[Double], bidYield : Option[Double], cfdBorrowingCost : Option[Double],
                                       cdHardToFinanceRate : Option[Double], cfdPriceAdjustment : Option[Boolean], dma : Option[Boolean], estPriceBuy : Option[Double],
                                       estPriceSell : Option[Double], expiryDate : Option[LocalDateTime], forwardDateFarLeg : Option[LocalDateTime],
                                       forwardDateNearLeg : Option[LocalDateTime], indexRatio : Option[Double], isMarketOpen : Option[Boolean], lowerBarrier : Option[Double],
                                       midForwardPrice : Option[Double], midSpotPrice : Option[Double], midYield : Option[Double], noticeDate : Option[LocalDateTime])

    case class InstrumentPriceDetailsPartial2(
                                               openInterest : Option[Double], paidCfdInterest : Option[Double], paidSrdInterest : Option[Double],
                                               receivedCfdInterest : Option[Double], receivedSrdInterest : Option[Double], shortTradeDisabled : Option[Boolean],
                                               spotAsk : Option[Double], spotBid : Option[Double], spotDate : Option[LocalDateTime], srdLastTradeDate : Option[LocalDateTime],
                                               srdSettlementDate : Option[LocalDateTime], strikePrice : Option[Double], swapAsk : Option[Double],
                                               swapBid : Option[Double],upperBarrier : Option[Double], valueDate : Option[LocalDateTime]
                                             )

    val part1Reads = Json.reads[InstrumentPriceDetailsPartial1]
    val part2reads = Json.reads[InstrumentPriceDetailsPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        InstrumentPriceDetailsPartial1(accruedInterest, askYield, bidYield, cfdBorrowingCost, cdHardToFinanceRate, cfdPriceAdjustment, dma, estPriceBuy,
        estPriceSell, expiryDate, forwardDateFarLeg, forwardDateNearLeg, indexRatio, isMarketOpen, lowerBarrier, midForwardPrice, midSpotPrice,
        midYield, noticeDate)
        ,
        InstrumentPriceDetailsPartial2(openInterest, paidCfdInterest, paidSrdInterest, receivedCfdInterest, receivedSrdInterest, shortTradeDisabled,
        spotAsk, spotBid, spotDate, srdLastTradeDate, srdSettlementDate, strikePrice, swapAsk, swapBid, upperBarrier, valueDate)
        ) =>
        InstrumentPriceDetails(accruedInterest, askYield, bidYield, cfdBorrowingCost, cdHardToFinanceRate, cfdPriceAdjustment, dma,
          estPriceBuy, estPriceSell, expiryDate, forwardDateFarLeg, forwardDateNearLeg, indexRatio, isMarketOpen, lowerBarrier, midForwardPrice,
          midSpotPrice, midYield, noticeDate, openInterest, paidCfdInterest, paidSrdInterest, receivedCfdInterest, receivedSrdInterest,
          shortTradeDisabled, spotAsk, spotBid, spotDate, srdLastTradeDate, srdSettlementDate, strikePrice, swapAsk, swapBid, upperBarrier, valueDate)
    }
  }

  object SubPositionBaseReads {
    case class SubPositionBasePartial1(accountId : String, accountKey : String, amount : Double, assetType: String, blockedQuantity : Double, canBeClosed : Boolean,
                                       clientId : String, closeConversionRateSettled : Boolean, contractId : Option[Long], correlationKey : Option[String],
                                       correlationTypes : Option[Seq[String]], executionTimeClose : Option[LocalDateTime], executionTimeOpen : Option[LocalDateTime],
                                       expiryDate : Option[LocalDateTime], externalReference : Option[String], fixedIncomeData : Option[FixedIncomeData],
                                       isForceOpen : Option[Boolean], isMarketOpen : Boolean, noticeDate : Option[LocalDateTime], openIndexRatio : Option[Double])

    case class SubPositionBasePartial2(openPrice : Double, openPriceIncludingCosts : Double, optionsData : Option[OptionsData], relatedOpenOrders : Option[Seq[RelatedOrderInfo]],
                                       relatedPositionId : Option[String], sourceOrderId : String, spotDate : LocalDate, srdLastTradeDate : Option[LocalDateTime],
                                       srdSettlementDate : Option[LocalDateTime], status : String, toOpenClose : Option[String], uic : Long, valueDate : LocalDateTime)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.fixedIncomeReads
    import AutoReads.optionsDataReads
    import AutoReads.relatedOrderReads

    val part1Reads = Json.reads[SubPositionBasePartial1]
    val part2reads = Json.reads[SubPositionBasePartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        SubPositionBasePartial1( accountId, accountKey, amount, assetType: String, blockedQuantity, canBeClosed, clientId, closeConversionRateSettled,
        contractId, correlationKey, correlationTypes, executionTimeClose, executionTimeOpen, expiryDate, externalReference, fixedIncomeData, isForceOpen,
        isMarketOpen, noticeDate, openIndexRatio)
        ,
        SubPositionBasePartial2(openPrice, openPriceIncludingCosts, optionsData, relatedOpenOrders, relatedPositionId, sourceOrderId, spotDate, srdLastTradeDate,
        srdSettlementDate, status, toOpenClose, uic, valueDate)
        ) =>
        SubPositionBase(accountId, accountKey, amount, assetType, blockedQuantity, canBeClosed, clientId, closeConversionRateSettled, contractId, correlationKey,
          correlationTypes, executionTimeClose, executionTimeOpen, expiryDate, externalReference, fixedIncomeData, isForceOpen, isMarketOpen, noticeDate,
          openIndexRatio, openPrice, openPriceIncludingCosts, optionsData, relatedOpenOrders, relatedPositionId, sourceOrderId, spotDate, srdLastTradeDate,
          srdSettlementDate, status, toOpenClose, uic, valueDate)
    }
  }


  object NetPositionDynamicRead {
    case class NetPositionDynamicPartial1(ask : Double, averageOpenPrice : Double, bid : Double, calculationReliability : Option[String], conversionRateCurrent : Double,
                                          currentPrice : Double, currentPriceDelayMinutes : Int, currentPriceLastTraded : LocalDateTime, currentPriceType : String,
                                          exposure : Double, exposureCurrency : String, exposureInBaseCurrency : Double, indexRatio : Option[Double],
                                          instrumentPriceDayPercentChange : Option[Double], marketValue : Double, marketValueInBaseCurrency : Double, marketValueOpen : Double,
                                          marketValueOpenInBaseCurrency : Double)

    case class NetPositionDynamicPartial2(openInterest : Option[Double], positionCount : Int, positionsNotClosedCount : Int, profitLossCurrencyConversion : Double,
                                          profitLossOnTrade : Double, profitLossOnTradeInBaseCurrency : Double, realizedCostsTotal : Double, realizedCostsTotalInBaseCurrency : Double,
                                          realizedProfitLoss : Double, realizedProfitLossInBaseCurrency : Double, settlementInstructions : Option[SettlementInstructions],
                                          status : String, tradeCostsTotal : Double, tradeCostsTotalInBaseCurrency : Double)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.settlementInstructionsReads

    val part1Reads = Json.reads[NetPositionDynamicPartial1]
    val part2reads = Json.reads[NetPositionDynamicPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        NetPositionDynamicPartial1(
        ask, averageOpenPrice, bid, calculationReliability, conversionRateCurrent, currentPrice, currentPriceDelayMinutes, currentPriceLastTraded,
        currentPriceType, exposure, exposureCurrency, exposureInBaseCurrency, indexRatio, instrumentPriceDayPercentChange,
        marketValue, marketValueInBaseCurrency, marketValueOpen, marketValueOpenInBaseCurrency)
        ,
        NetPositionDynamicPartial2(openInterest, positionCount, positionsNotClosedCount, profitLossCurrencyConversion, profitLossOnTrade,
        profitLossOnTradeInBaseCurrency, realizedCostsTotal, realizedCostsTotalInBaseCurrency, realizedProfitLoss, realizedProfitLossInBaseCurrency,
        settlementInstructions, status, tradeCostsTotal, tradeCostsTotalInBaseCurrency)
        ) =>
        NetPositionDynamic(ask, averageOpenPrice, bid, calculationReliability, conversionRateCurrent, currentPrice, currentPriceDelayMinutes,
          currentPriceLastTraded, currentPriceType, exposure, exposureCurrency, exposureInBaseCurrency, indexRatio, instrumentPriceDayPercentChange,
          marketValue, marketValueInBaseCurrency, marketValueOpen, marketValueOpenInBaseCurrency, openInterest, positionCount, positionsNotClosedCount,
          profitLossCurrencyConversion, profitLossOnTrade, profitLossOnTradeInBaseCurrency, realizedCostsTotal, realizedCostsTotalInBaseCurrency, realizedProfitLoss,
          realizedProfitLossInBaseCurrency, settlementInstructions, status, tradeCostsTotal, tradeCostsTotalInBaseCurrency)
    }
  }

  object PositionDynamicReads {
    case class PositionDynamicPartial1(ask : Double, averageOpenPrice : Double, bid : Double, calculationReliability : Option[String], conversionRateCurrent : Double,
                                       currentPrice : Double, currentPriceDelayMinutes : Int, currentPriceLastTraded : LocalDateTime, currentPriceType : String,
                                       exposure : Double, exposureCurrency : String, exposureInBaseCurrency : Double, indexRatio : Option[Double],
                                       instrumentPriceDayPercentChange : Option[Double], marketValue : Double, marketValueInBaseCurrency : Double, marketValueOpen : Double,
                                       marketValueOpenInBaseCurrency : Double)

    case class PositionDynamicPartial2(openInterest : Option[Double], positionCount : Int, positionsNotClosedCount : Int, profitLossCurrencyConversion : Double,
                                       profitLossOnTrade : Double, profitLossOnTradeInBaseCurrency : Double, realizedCostsTotal : Double, realizedCostsTotalInBaseCurrency : Double,
                                       realizedProfitLoss : Double, realizedProfitLossInBaseCurrency : Double, settlementInstructions : Option[SettlementInstructions],
                                       status : String, tradeCostsTotal : Double, tradeCostsTotalInBaseCurrency : Double)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.settlementInstructionsReads

    val part1Reads = Json.reads[PositionDynamicPartial1]
    val part2reads = Json.reads[PositionDynamicPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        PositionDynamicPartial1(ask, averageOpenPrice, bid, calculationReliability, conversionRateCurrent, currentPrice, currentPriceDelayMinutes,
        currentPriceLastTraded, currentPriceType, exposure, exposureCurrency, exposureInBaseCurrency, indexRatio, instrumentPriceDayPercentChange,
        marketValue, marketValueInBaseCurrency, marketValueOpen, marketValueOpenInBaseCurrency)
        ,
        PositionDynamicPartial2(openInterest, positionCount, positionsNotClosedCount, profitLossCurrencyConversion, profitLossOnTrade,
        profitLossOnTradeInBaseCurrency, realizedCostsTotal, realizedCostsTotalInBaseCurrency, realizedProfitLoss, realizedProfitLossInBaseCurrency,
        settlementInstructions, status, tradeCostsTotal, tradeCostsTotalInBaseCurrency)
        ) =>
        PositionDynamic(ask, averageOpenPrice, bid, calculationReliability, conversionRateCurrent, currentPrice, currentPriceDelayMinutes, currentPriceLastTraded,
          currentPriceType, exposure, exposureCurrency, exposureInBaseCurrency, indexRatio, instrumentPriceDayPercentChange, marketValue, marketValueInBaseCurrency,
          marketValueOpen, marketValueOpenInBaseCurrency, openInterest, positionCount, positionsNotClosedCount, profitLossCurrencyConversion, profitLossOnTrade,
          profitLossOnTradeInBaseCurrency, realizedCostsTotal, realizedCostsTotalInBaseCurrency, realizedProfitLoss, realizedProfitLossInBaseCurrency,
          settlementInstructions, status, tradeCostsTotal, tradeCostsTotalInBaseCurrency)
    }
  }

  object NetPositionStaticReads {
    case class NetPositionStaticPartial1(accountId : Option[String], amount : Double, amountLong : Option[Double], amountShort : Option[Double], assetType: String,
                                         blockedQuantity : Double, canBeClosed : Boolean, clientId : String, expiryDate : Option[LocalDateTime], fixedIncomeData : Option[FixedIncomeData],
                                         hasForceOpenPositions : Option[Boolean], isMarketOpen : Boolean, marketState : String, nonTradableReason : Option[String], noticeDate : Option[LocalDateTime],
                                         numberOfRelatedOrders : Int, openIndexRatioAverage : Option[Double], openIpoOrdersCount : Option[Int], openOrdersCount : Option[Int],
                                         openTriggerOrdersCount : Option[Int])

    case class NetPositionStaticPartial2(optionsData : Option[OptionsData], positionsAccount : Option[String], shortTrading : Option[String], singlePositionAccountId : Option[String],
                                         singlePositionId : Option[String], singlePositionStatus : Option[String], srdLastTradeDate : Option[LocalDateTime], srdSettlementDate : Option[LocalDateTime],
                                         tradingStatus : String, uic : Long, valueDate : LocalDateTime)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.fixedIncomeReads
    import AutoReads.optionsDataReads

    val part1Reads = Json.reads[NetPositionStaticPartial1]
    val part2reads = Json.reads[NetPositionStaticPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        NetPositionStaticPartial1(accountId, amount, amountLong, amountShort, assetType, blockedQuantity, canBeClosed, clientId, expiryDate, fixedIncomeData,
        hasForceOpenPositions, isMarketOpen, marketState, nonTradableReason, noticeDate, numberOfRelatedOrders, openIndexRatioAverage, openIpoOrdersCount,
        openOrdersCount, openTriggerOrdersCount)
        ,
        NetPositionStaticPartial2(optionsData, positionsAccount, shortTrading, singlePositionAccountId, singlePositionId, singlePositionStatus,
        srdLastTradeDate, srdSettlementDate, tradingStatus, uic, valueDate)
        ) =>
        NetPositionStatic(accountId, amount, amountLong, amountShort, assetType, blockedQuantity, canBeClosed, clientId, expiryDate, fixedIncomeData,
          hasForceOpenPositions, isMarketOpen, marketState, nonTradableReason, noticeDate, numberOfRelatedOrders, openIndexRatioAverage, openIpoOrdersCount,
          openOrdersCount, openTriggerOrdersCount, optionsData, positionsAccount, shortTrading, singlePositionAccountId, singlePositionId, singlePositionStatus,
          srdLastTradeDate, srdSettlementDate, tradingStatus, uic, valueDate)
    }
  }

  object PositionStaticReads {
    case class PositionStaticPartial1(accountId : String, accountKey : String, amount : Double, assetType: String, canBeClosed : Boolean, clientId : String,
                                      closeConversionRateSettled : Option[Boolean], contractId : Option[Long], correlationKey : Option[String],
                                      correlationTypes : Option[Seq[String]], executionTimeClose : Option[LocalDateTime], executionTimeOpen : Option[LocalDateTime],
                                      expiryDate : Option[LocalDateTime], fixedIncomeData : Option[FixedIncomeData], isForceOpen : Option[Boolean],
                                      isMarketOpen : Boolean, marketState : String, noticeDate : Option[LocalDateTime], openIndexRatio : Option[Double],
                                      openPrice : Option[Double], openPriceIncludingCosts : Option[Double])

    case class PositionStaticPartial2( optionsData : Option[OptionsData], relatedOptionOrders : Option[Seq[RelatedOrderInfo]], sourceOrderId : Option[String], spotDate : Option[LocalDateTime],
                                      srdLastTradeDate : Option[LocalDateTime], srdSettlementDate : Option[LocalDateTime], status : String,
                                      toOpenClose : Option[String], uic : Long, valueDate : LocalDateTime)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.fixedIncomeReads
    import AutoReads.optionsDataReads
    import AutoReads.relatedOrderReads

    val part1Reads = Json.reads[PositionStaticPartial1]
    val part2reads = Json.reads[PositionStaticPartial2]

    val reads = (part1Reads ~ part2reads).tupled.map {
      case (
        PositionStaticPartial1(accountId, accountKey, amount, assetType, canBeClosed, clientId, closeConversionRateSettled, contractId, correlationKey,
        correlationTypes, executionTimeClose, executionTimeOpen, expiryDate, fixedIncomeData, isForceOpen, isMarketOpen, marketState, noticeDate,
        openIndexRatio, openPrice, openPriceIncludingCosts)
        ,
        PositionStaticPartial2(optionsData, relatedOptionOrders, sourceOrderId, spotDate, srdLastTradeDate, srdSettlementDate,
        status, toOpenClose, uic, valueDate)
        ) =>
        PositionStatic(accountId, accountKey, amount, assetType, canBeClosed, clientId, closeConversionRateSettled, contractId,
          correlationKey, correlationTypes, executionTimeClose, executionTimeOpen, expiryDate, fixedIncomeData, isForceOpen, isMarketOpen,
          marketState, noticeDate, openIndexRatio, openPrice, openPriceIncludingCosts, optionsData, relatedOptionOrders, sourceOrderId, spotDate,
          srdLastTradeDate, srdSettlementDate, status, toOpenClose, uic, valueDate)
    }
  }


  object BalanceTopicReads {
    case class BalanceTopicPartial1(accountFundingLimit : Double, accountNetFundedAmount : Double, accountRemainingFunding : Double,
                                    accountValueProtectionLimit : Double, calculationReliability : String, cashAvailableForTrading : Double,
                                    cashBalance : Double, cashBlocked : Double, changesScheduled : Boolean, closedPositionsCount : Int,
                                    collateralAvailable : Double, collateralCreditLine : LineStatus, collateralCreditValue : LineStatus,
                                    collateralLoan : Double, corporateActionUnrealizedAmounts : Double, costToClosePositions : Double,
                                    creditLine : LineStatus, currency : String, currencyDecimals : Int, firstFundingDate : LocalDateTime)

    case class BalanceTopicPartial2(fundsAvailableForSettlement : Double, fundsReservedForSettlement : Double, initialMargin : InitialMargin,
                                    intradayMarginDiscount : Double, isPortfolioMarginModelSimple : Boolean, marginAvailableForTrading : Double,
                                    marginCollateralNotAvailable : Double, marginCollateralNotAvailableDetail : MarginCollateralNotAvailableDetail,
                                    marginExposureCoveragePct : Double, marginNetExposure : Double, marginOverview : MarginOverviewGroup,
                                    marginUsedByCurrentPositions : Double, marginUtilizationPct : Double, netEquityForMargin : Double,
                                    netPositionsCount : Int, nonMarginPositionsValue : Double, openIpoOrdersCount : Int, openPositionsCount : Int)

    case class BalanceTopicPartial3(optionPremiumsMarketValue : Double, ordersCount : Int, otherCollateral : Double, otherCollateralDeduction : Double,
                                    settlementLine : LineStatus, settlementValue : Double, spendingPower : Double, totalRiskLine : LineStatus,
                                    totalValue : Double, tradingLine : LineStatus, transactionsNotBooked : Double, transactionsNotBookedDetail : TransactionsNotBookedDetails,
                                    triggerOrdersCount : Int, unrealizedMarginClosedProfitLoss : Double, unrealizedMarginOpenProfitLoss : Double,
                                    unrealizedMarginProfitLoss : Double, unrealizedPositionsValue : Double, variationMargin : Double,
                                    variationMarginCashBalance : Double, variationMarginThreshold : Double)

    import finalibre.saxo.rest.outgoing.streaming.JsonTransformations.{Reads => AutoReads}
    import AutoReads.lineStatusReads
    import AutoReads.initialMarginReads
    import AutoReads.marginCollateralNotAvailableReads
    import AutoReads.marginOverviewGroupReads
    import AutoReads.transactionsNotBookedReads

    val part1Reads = Json.reads[BalanceTopicPartial1]
    val part2reads = Json.reads[BalanceTopicPartial2]
    val part3reads = Json.reads[BalanceTopicPartial3]

    val reads = (part1Reads ~ part2reads ~ part3reads).tupled.map {
      case (
        BalanceTopicPartial1(accountFundingLimit, accountNetFundedAmount, accountRemainingFunding, accountValueProtectionLimit, calculationReliability,
        cashAvailableForTrading, cashBalance, cashBlocked, changesScheduled, closedPositionsCount, collateralAvailable, collateralCreditLine,
        collateralCreditValue, collateralLoan, corporateActionUnrealizedAmounts, costToClosePositions, creditLine, currency, currencyDecimals, firstFundingDate)
        ,
        BalanceTopicPartial2(fundsAvailableForSettlement, fundsReservedForSettlement, initialMargin, intradayMarginDiscount, isPortfolioMarginModelSimple,
        marginAvailableForTrading, marginCollateralNotAvailable, marginCollateralNotAvailableDetail, marginExposureCoveragePct, marginNetExposure,
        marginOverview, marginUsedByCurrentPositions, marginUtilizationPct, netEquityForMargin, netPositionsCount, nonMarginPositionsValue, openIpoOrdersCount, openPositionsCount)
        ,
        BalanceTopicPartial3(optionPremiumsMarketValue, ordersCount, otherCollateral,
          otherCollateralDeduction, settlementLine, settlementValue, spendingPower, totalRiskLine, totalValue, tradingLine, transactionsNotBooked,
          transactionsNotBookedDetail, triggerOrdersCount, unrealizedMarginClosedProfitLoss, unrealizedMarginOpenProfitLoss, unrealizedMarginProfitLoss,
          unrealizedPositionsValue, variationMargin, variationMarginCashBalance, variationMarginThreshold)
        ) =>
        BalanceTopic(accountFundingLimit, accountNetFundedAmount, accountRemainingFunding, accountValueProtectionLimit, calculationReliability,
          cashAvailableForTrading, cashBalance, cashBlocked, changesScheduled, closedPositionsCount, collateralAvailable, collateralCreditLine,
          collateralCreditValue, collateralLoan, corporateActionUnrealizedAmounts, costToClosePositions, creditLine, currency, currencyDecimals,
          firstFundingDate, fundsAvailableForSettlement, fundsReservedForSettlement, initialMargin, intradayMarginDiscount, isPortfolioMarginModelSimple,
          marginAvailableForTrading, marginCollateralNotAvailable, marginCollateralNotAvailableDetail, marginExposureCoveragePct, marginNetExposure,
          marginOverview, marginUsedByCurrentPositions, marginUtilizationPct, netEquityForMargin, netPositionsCount, nonMarginPositionsValue,
          openIpoOrdersCount, openPositionsCount, optionPremiumsMarketValue, ordersCount, otherCollateral, otherCollateralDeduction,
          settlementLine, settlementValue, spendingPower, totalRiskLine, totalValue, tradingLine, transactionsNotBooked, transactionsNotBookedDetail,
          triggerOrdersCount, unrealizedMarginClosedProfitLoss, unrealizedMarginOpenProfitLoss, unrealizedMarginProfitLoss, unrealizedPositionsValue,
          variationMargin, variationMarginCashBalance, variationMarginThreshold)
    }
  }


}
