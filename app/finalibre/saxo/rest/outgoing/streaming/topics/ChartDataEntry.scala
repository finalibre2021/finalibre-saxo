package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ChartDataEntry(
                      close : Option[Double],
                      closeAsk : Double,
                      closeBid : Double,
                      growth : Option[Double],
                      high : Option[Double],
                      highAsk : Double,
                      highBid : Double,
                      interest : Option[Double],
                      low : Option[Double],
                      lowAsk : Double,
                      lowBid : Double,
                      open : Option[Double],
                      openAsk : Double,
                      openBid : Double,
                      time : LocalDateTime,
                      volume : Option[Double]
                    )
