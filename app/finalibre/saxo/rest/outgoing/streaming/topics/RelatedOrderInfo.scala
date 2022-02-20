package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class RelatedOrderInfo(
                             amount : Double,
                             duration : OrderDuration,
                             openOrderType : String,
                             orderId : String,
                             orderPrice : Double,
                             stopLimitPrice : Double,
                             trailingStopDistanceToMarket : Option[Double],
                             trailingStopStep : Option[Double]
                           )

