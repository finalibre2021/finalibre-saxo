package finalibre.saxo.rest.outgoing.streaming.topics

case class InvestmentSuggestionTopic(
                                      autoTradingPartnerLeaderId : Option[String],
                                      clientId : Long,
                                      currency : String,
                                      isAuthorizedToFollow : Boolean,
                                      isFollowAllowed : Boolean,
                                      isFollowing : Boolean,
                                      isOpenForFollowers : Boolean,
                                      isReadyForTrading : Boolean,
                                      isTradeFollowerReady : Boolean,
                                      minimumFunding : Double,
                                      strategyName : Option[String],
                                      tradeLeaderId : Option[String]
                                    ) extends StreamingTopic
