package finalibre.saxo.rest.outgoing.streaming.topics

case class PriceInfo(
                      high : Option[Double],
                      low : Option[Double],
                      netChange : Option[Double],
                      percentChange : Option[Double]

                    )
