package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class PriceTopic(
                     assetType : String,
                     lastUpdate : LocalDateTime,
                     priceSource : String,
                     uic : Long,
                     quote : PriceQuote
                     ) extends StreamingTopic
