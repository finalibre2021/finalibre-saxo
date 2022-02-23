package finalibre.saxo.rest.outgoing.streaming.topics

case class PriceInfoDetails(
                           askSize : Option[Double],
                           askYield : Option[Double],
                           bidSize : Option[Double],
                           bidYield : Option[Double],
                           lastClose : Option[Double],
                           lastTraded : Option[Double],
                           lastTradedSize : Option[Double],
                           volume : Option[Double]
                           )
