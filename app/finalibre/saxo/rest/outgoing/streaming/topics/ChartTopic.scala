package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ChartTopic(
                     chartInfo : Option[ChartInfo],
                     data : Seq[ChartDataEntry],
                     dataVersion : Option[Long],
                     displayAndFormat: Option[ChartDisplayAndFormat]
                     ) extends StreamingTopic



