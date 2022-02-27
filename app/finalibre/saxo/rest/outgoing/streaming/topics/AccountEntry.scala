package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class AccountEntry(
                         accountGroupKey : String,
                         accountId : String,
                         accountKey : String,
                         accountType : String,
                         active : Boolean,
                         canUseCashPositionsAsMarginCollateral : Boolean,
                         cfdBorrowingCostsActive : Boolean,
                         clientId : String,
                         clientKey : String,
                         creationDate : LocalDateTime,
                         currency : String,
                         currencyDecimals : Int,
                         directMarketAccess : Boolean,
                         fractionalOrderEnabled : Boolean,
                         individualMargining : Boolean,
                         isCurrencyConversionAtSettlementTime : Boolean,
                         isMarginTradingAllowed : Boolean,
                         isShareable : Boolean,
                         isTrialAccount : Boolean,
                         legalAssetType : Seq[String],
                         managementType : String,
                         marginCalculationMethod : String,
                         marginLendingEnabled : String,
                         sharing : Seq[String],
                         supportsAccountValueProtectionLimit : Boolean,
                         useCashPositionsAsMarginCollateral : Boolean
                       )

object AccountEntry {
  case class AccountEntryPartialPart1(
                                  accountGroupKey : String,
                                  accountId : String,
                                  accountKey : String,
                                  accountType : String,
                                  active : Boolean,
                                  canUseCashPositionsAsMarginCollateral : Boolean,
                                  cfdBorrowingCostsActive : Boolean,
                                  clientId : String,
                                  clientKey : String,
                                  creationDate : LocalDateTime,
                                  currency : String,
                                  currencyDecimals : Int,
                                  directMarketAccess : Boolean,
                                  fractionalOrderEnabled : Boolean,
                                  individualMargining : Boolean,
                                  isCurrencyConversionAtSettlementTime : Boolean,
                                  isMarginTradingAllowed : Boolean,
                                  isShareable : Boolean,
                                  isTrialAccount : Boolean,
                                  legalAssetType : Seq[String]
                                )
  case class AccountEntryPartialPart2(
                                managementType : String,
                                marginCalculationMethod : String,
                                marginLendingEnabled : String,
                                sharing : Seq[String],
                                supportsAccountValueProtectionLimit : Boolean,
                                useCashPositionsAsMarginCollateral : Boolean
                              )
}