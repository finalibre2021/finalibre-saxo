package finalibre.saxo.rest.outgoing.streaming.topics

case class TradableQuote(
                          amount : Option[Long],
                          ask : Option[Double],
                          bid : Option[Double],
                          delayedByMinutes : Option[Int],
                          errorCode : Option[String],
                          mid : Option[Double],
                          priceSource : Option[String],
                          priceTypeAsk : Option[String],
                          priceTypeBid : Option[String],
                          quoteId : Option[String],
                          referencePrice : Option[Double],
                          rFQState : Option[String]
                        )
