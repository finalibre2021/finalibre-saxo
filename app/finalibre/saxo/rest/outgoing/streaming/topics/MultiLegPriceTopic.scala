package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class MultiLegPriceTopic(
                             buySell : Option[String],
                             commissions : Option[Commissions],
                             greeks : Option[GreeksDetails],
                             instrumentPriceDetails: Option[InstrumentPriceDetails],
                             lastUpdated : Option[LocalDateTime],
                             legs : Option[MultiLegOrderPriceLeg],
                             marginImpactBuySell : Option[MarginImpactBuySell],
                             marketState : Option[String],
                             priceSource : Option[String],
                             quote : Option[Quote],
                             strategyType : Option[String]
                             ) extends StreamingTopic
