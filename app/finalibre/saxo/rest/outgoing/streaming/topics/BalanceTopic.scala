package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class BalanceTopic(
                         accountFundingLimit : Double,
                         accountNetFundedAmount : Double,
                         accountRemainingFunding : Double,
                         accountValueProtectionLimit : Double,
                         calculationReliability : String,
                         cashAvailableForTrading : Double,
                         cashBalance : Double,
                         cashBlocked : Double,
                         changesScheduled : Boolean,
                         closedPositionsCount : Int,
                         collateralAvailable : Double,
                         collateralCreditLine : LineStatus,
                         collateralCreditValue : LineStatus,
                         collateralLoan : Double,
                         corporateActionUnrealizedAmounts : Double,
                         costToClosePositions : Double,
                         creditLine : LineStatus,
                         currency : String,
                         currencyDecimals : Int,
                         firstFundingDate : LocalDateTime,
                         fundsAvailableForSettlement : Double,
                         fundsReservedForSettlement : Double,
                         initialMargin : InitialMargin,
                         intradayMarginDiscount : Double,
                         isPortfolioMarginModelSimple : Boolean,
                         marginAvailableForTrading : Double,
                         marginCollateralNotAvailable : Double,
                         marginCollateralNotAvailableDetail : MarginCollateralNotAvailableDetail,
                         marginExposureCoveragePct : Double,
                         marginNetExposure : Double,
                         marginOverview : MarginOverviewGroup,
                         marginUsedByCurrentPositions : Double,
                         marginUtilizationPct : Double,
                         netEquityForMargin : Double,
                         netPositionsCount : Int,
                         nonMarginPositionsValue : Double,
                         openIpoOrdersCount : Int,
                         openPositionsCount : Int,
                         optionPremiumsMarketValue : Double,
                         ordersCount : Int,
                         otherCollateral : Double,
                         otherCollateralDeduction : Double,
                         settlementLine : LineStatus,
                         settlementValue : Double,
                         spendingPower : Double,
                         totalRiskLine : LineStatus,
                         totalValue : Double,
                         tradingLine : LineStatus,
                         transactionsNotBooked : Double,
                         transactionsNotBookedDetail : TransactionsNotBookedDetails,
                         triggerOrdersCount : Int,
                         unrealizedMarginClosedProfitLoss : Double,
                         unrealizedMarginOpenProfitLoss : Double,
                         unrealizedMarginProfitLoss : Double,
                         unrealizedPositionsValue : Double,
                         variationMargin : Double,
                         variationMarginCashBalance : Double,
                         variationMarginThreshold : Double
                       ) extends StreamingTopic











