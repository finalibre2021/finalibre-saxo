package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ChartInfo(
                      delayedByMinutes : Int,
                      exchangeId : String,
                      firstSampleTime : LocalDateTime,
                      horizon : Option[Int]
                    )
