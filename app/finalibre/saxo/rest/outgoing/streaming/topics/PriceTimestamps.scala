package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class PriceTimestamps(
                            askTime : Option[LocalDateTime],
                            bidTime : Option[LocalDateTime],
                            closeTime : Option[LocalDateTime],
                            highTime : Option[LocalDateTime],
                            lastTradedVolumeTime : Option[LocalDateTime],
                            lowTime : Option[LocalDateTime],
                            openPriceTime : Option[LocalDateTime],
                            underlyingUicLastTradeTime : Option[LocalDateTime]
                          )
