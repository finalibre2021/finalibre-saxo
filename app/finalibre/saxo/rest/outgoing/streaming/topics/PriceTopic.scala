package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class PriceTopic(
                       assetType : String,
                       commissions : Option[Commissions],
                       displayAndFormat: DisplayAndFormat,
                       greeks : Option[GreeksDetails],
                       historicalChanges : Option[HistoricalChanges],
                       instrumentPriceDetails : Option[InstrumentPriceDetails],
                       lastUpdate : LocalDateTime,
                       marginImpactBuySell : Option[MarginImpactBuySell],
                       marketDepth: Option[MarketDepth],
                       priceInfo : Option[PriceInfo],
                       priceInfoDetails : Option[PriceInfoDetails],
                       priceSource : Option[String],
                       quote : Option[TradableQuote],
                       timestamps : Option[PriceTimestamps],
                       uic : Long
                     ) extends StreamingTopic
