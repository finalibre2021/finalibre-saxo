package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.{LocalDate, LocalDateTime}

case class PositionTopic(
                           displayAndFormat: Option[InstrumentDisplayAndFormat],
                           exchange: Option[InstrumentExchangeDetails],
                           greeks : Option[GreeksDetails],
                           netPositionId : String,
                           positionBase : Option[PositionStatic],
                           positionId : String,
                           positionView : Option[PositionDynamic]
                           ) extends StreamingTopic



