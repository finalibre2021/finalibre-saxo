package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class FxOptionsBaseData(
                              barrierEventOccurred : Boolean,
                              expiryCut : String,
                              expiryDate : LocalDateTime,
                              lowerBarrier : Double,
                              putCall : String,
                              strike : Double,
                              upperBarrier : Double
                            )
