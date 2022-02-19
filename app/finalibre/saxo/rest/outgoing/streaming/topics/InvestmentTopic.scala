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

/*
EntryGateResult 	EntryGateResult 	Entry Gate Test Result
ErrorCode 	ErrorCode 	Error Code
ErrorNumber 	Int 	Error number if this InvestmentContract is in error state This property is obsolete, please use ErrorCode
InitialFunding 	Decimal 	The initial funding amount.
InvestmentId 	String 	Investment Item identifier
InvestmentProcessState 	InvestmentProcessState 	Investment process state
InvestmentShieldAmount 	Decimal 	Investment shield amount (drawdown protection level).
InvestmentStateId 	Int 	State Id of this InvestmentContract
IsAuthorizedToFollow 	Bool 	Reflects legal aspects of client (suitability and terms/conditions)
IsAutoFundsTransfer 	Bool 	Information about whether the funds would be auto transferred in case of investment stop.
IsFollowAllowed 	Bool 	Indicates whether user is allowed to follow current leaders instrument types and instruments. Also evaluates if follower passes any applied country filter or client group restrictions on leader. And verifies leader is enabled for any of followers owner(s).
IsFollowing 	Bool 	Indicates if this InvestmentContract is actively following.
IsOpenForFollowers 	Bool 	Indicates if additional followers can join.
IsReadyForTrading 	Bool 	Indictes if this InvestmentContract is ready for trading.
IsTradeFollowerReady 	Bool 	Indicates Trade Follower created, active and passed suitability test
IsWithdrawalInProgress 	Bool 	Indicates if cash wtihdrawal is in progress.
LeaderLogoUrl 	String 	Logo Url of the respective trade leader
MinimumFunding 	Decimal 	The minimum funding amount.
PendingFunding 	Decimal 	Any pending funding.
PositionsCloseOnDeactivation 	Bool 	Information about whether to close or keep related positions open on the investment deactivation (not available to all)
ReservedAmount 	Decimal 	Any amount marked as reserved for withdrawl
ReturnPercentage 	Decimal 	Account Performance return fraction
StateName 	String 	Description of the state this InvestmentContract is in
StrategyName 	String 	Name of the strategy.
TradeLeaderId 	String 	TradeLeader identifier.

 */