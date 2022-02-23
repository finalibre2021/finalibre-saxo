package finalibre.saxo.rest.outgoing.streaming.topics

case class MultiLegOrderPriceLeg(
                                amount : Option[Double],
                                assetType : Option[String],
                                buySell : Option[String],
                                greeks : Option[GreeksDetails],
                                instrumentPriceDetails : Option[InstrumentPriceDetails],
                                legId : String,
                                quote : Option[Quote],
                                toOpenClose : Option[String],
                                uic : Option[Long]
                                )
