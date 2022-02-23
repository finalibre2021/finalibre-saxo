package finalibre.saxo.rest.outgoing.streaming.topics

case class Quote(
                  amount : Double,
                  ask : Double,
                  bid : Double,
                  delayedInMinutes : Long,
                  errorCode : Option[String],
                  marketState : String,
                  mid : Double,
                  priceSource : Option[String],
                  priceTypeAsk : Option[String],
                  priceTypeBid : Option[String],
                  referencePrice : Option[Double]
                )
