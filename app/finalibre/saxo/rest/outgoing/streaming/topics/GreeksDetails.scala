package finalibre.saxo.rest.outgoing.streaming.topics

case class GreeksDetails(
                          Delta : Double,
                          DeltaCurrency : String,
                          Gamma : Double,
                          GammaCurrency : String,
                          InstrumentDelta : Double,
                          InstrumentGamma : Double,
                          InstrumentTheta : Double,
                          InstrumentVega : Double,
                          MidVol : Double,
                          Phi : Double,
                          Rho : Double,
                          TheoreticalPrice : Double,
                          Theta : Double,
                          ThetaCurrency : String,
                          Vega : Double,
                          VegaCurrency : String
                        )
