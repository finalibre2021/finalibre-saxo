package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class ExpiryData(
                       displayDate : Option[LocalDateTime],
                       displayDaysToExpiry : Option[Int],
                       expiry : Option[LocalDateTime],
                       index : Option[Int],
                       lastTradeDate : Option[LocalDateTime],
                       midStrikePrice : Option[Double],
                       rootIdentifier : Option[Long],
                       strikeCount : Option[Int],
                       underlyingUic : Option[Long]
)
