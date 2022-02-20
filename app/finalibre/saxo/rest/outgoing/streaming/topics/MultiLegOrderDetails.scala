package finalibre.saxo.rest.outgoing.streaming.topics

case class MultiLegOrderDetails(
                                 Amount : Double,
                                 BuySell : String,
                                 CurrentPrice : Double,
                                 Description : String,
                                 DistanceToMarket : Double,
                                 FilledAmount : Double,
                                 LegCount : Int,
                                 MultiLegOrderId : String,
                                 Price : Double,
                                 StrategyType : Option[String]
                               )
