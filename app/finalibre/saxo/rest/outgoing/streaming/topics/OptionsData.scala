package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class OptionsData(
                        barrierEventOccurred : Option[Boolean],
                        canBeExercised : Boolean,
                        exerciseStyle : String,
                        expiryCut : String,
                        expiryDate : LocalDateTime,
                        lowerBarrier : Option[Double],
                        premiumDate : Option[LocalDateTime],
                        putCall : String,
                        settlementStyle : String,
                        strike : Double,
                        upperBarrier : Option[Double]
                      )
