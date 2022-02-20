package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ChartTopic(
                     chartInfo : ChartInfo,
                     data : Seq[DataEntry],
                     dataVersion : Long,
                     displayAndFormat: DisplayAndFormat
                     ) extends StreamingTopic

case class ChartInfo(
                    delayedByMinutes : Int,
                    exchangeId : String,
                    firstSampleTime : LocalDateTime,
                    horizon : Option[Int]
                    )

case class DataEntry(
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

case class DisplayAndFormat(
                           currency : String,
                           decimals : Int,
                           description : String,
                           format : String,
                           symbol : String
                           )
