package finalibre.saxo.rest.outgoing.streaming.topics

case class InstrumentCollateralDetail(
                                       assetType : String,
                                       description : String,
                                       initialCollateral : Double,
                                       initialCollateralNotAvailable : Double,
                                       maintenanceCollateral : Double,
                                       maintenanceCollateralNotAvailable : Double,
                                       marketValue : Double,
                                       symbol : String,
                                       uic : Long
                                     )
