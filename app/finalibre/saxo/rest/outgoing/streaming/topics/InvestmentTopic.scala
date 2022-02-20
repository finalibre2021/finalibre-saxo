package finalibre.saxo.rest.outgoing.streaming.topics

case class InvestmentTopic(
                          accountId : String,
                          accountKey : String,
                          autoTradingPartnerLeaderId : Option[String],
                          clientId : Long,
                          currency : String,
                          displayName : String,
                          displayState : Option[Int],
                          entryGateResult : Option[String],
                          errorNumber : Int,
                          initialFunding : Double,
                          investmentId : String,
                          investmentProcessState : String,
                          investmentShieldAmount : Double,
                          investmentStateId : Int,
                          isAuthorizedToFollow : Boolean,
                          isFollowing : Boolean,
                          isOpenForFollowers : Boolean,
                          isReadyForTrading : Boolean,
                          isTradeFollowerReady : Boolean,
                          isWithdrawalInProgress : Boolean,
                          minimumFunding : Double,
                          pendingFunding : Double,
                          reservedAmount : Double,
                          stateName : String,
                          strategyName : Option[String],
                          tradeLeaderId : Option[String]
                          ) extends StreamingTopic

