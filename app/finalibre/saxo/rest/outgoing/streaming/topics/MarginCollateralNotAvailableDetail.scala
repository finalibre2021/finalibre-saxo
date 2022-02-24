package finalibre.saxo.rest.outgoing.streaming.topics

case class MarginCollateralNotAvailableDetail(
                                               initialFxHaircut : Double,
                                               maintenanceFxHaircut : Double,
                                               instrumentCollateralDetails : Seq[InstrumentCollateralDetail]
                                             )
