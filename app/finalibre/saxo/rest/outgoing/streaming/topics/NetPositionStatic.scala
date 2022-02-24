package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

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

