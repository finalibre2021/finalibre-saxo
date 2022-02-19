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
                    firstSampleTime : LocalDateTime
                    )

case class DataEntry(
                    closeAsk : Double,
                    closeBid : Double,
                    highAsk : Double,
                    highBid : Double,
                    lowAsk : Double,
                    lowBid : Double,
                    openAsk : Double,
                    openBid : Double,
                    time : LocalDateTime
)

case class DisplayAndFormat(
                           currency : String,
                           decimals : Int,
                           description : String,
                           format : String,
                           symbol : String
                           )
