package finalibre.saxo.rest.outgoing.streaming.topics

case class PriceQuote(
                     amount : Double,
                     ask : Double,
                     bid : Double,
                     delayedInMinutes : Long,
                     errorCode : Option[String],
                     marketState : String,
                     mid : Double,
                     priceSource : Option[String],
                     priceSourceType : Option[String],
                     priceTypeAsk : Option[String],
                     priceTypeBid : Option[String],
                     rfqState : Option[String]
                     )
