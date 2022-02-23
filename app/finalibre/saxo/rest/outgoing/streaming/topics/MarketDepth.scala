package finalibre.saxo.rest.outgoing.streaming.topics

case class MarketDepth(
                        ask : Option[Seq[Double]],
                        askOrders : Option[Seq[Double]],
                        askSize : Option[Seq[Double]],
                        bid : Option[Seq[Double]],
                        bidOrders : Option[Seq[Double]],
                        bidSize : Option[Seq[Double]],
                        noOfBids : Option[Int],
                        noOfOffers : Option[Int],
                        usingOrders : Option[Boolean]
                      )
