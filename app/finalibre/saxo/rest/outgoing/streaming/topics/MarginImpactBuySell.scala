package finalibre.saxo.rest.outgoing.streaming.topics

case class MarginImpactBuySell(
                                currency : String,
                                initialMarginAvailableBuy : Option[Double],
                                initialMarginAvailableCurrent : Option[Double],
                                initialMarginAvailableSell : Option[Double],
                                initialMarginBuy : Option[Double],
                                initialMarginSell : Option[Double],
                                initialSpendingPower : Option[Double],
                                maintenanceMarginBuy : Option[Double],
                                maintenanceMarginSell : Option[Double],
                                maintenanceSpendingPower : Option[Double],
                                marginBuy : Option[Double],
                                marginSell : Option[Double],
                                spendingPower : Option[Double]
                              )
