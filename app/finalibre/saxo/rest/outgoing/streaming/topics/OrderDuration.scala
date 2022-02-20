package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class OrderDuration(
                          durationType : String,
                          expirationDateContainsTime : Boolean,
                          expirationDateTime : LocalDateTime
                        )
