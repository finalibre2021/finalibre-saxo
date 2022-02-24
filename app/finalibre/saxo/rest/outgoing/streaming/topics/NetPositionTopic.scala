package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.{LocalDate, LocalDateTime}

case class NetPositionTopic(
                           displayAndFormat: Option[InstrumentDisplayAndFormat],
                           exchange: Option[InstrumentExchangeDetails],
                           greeks : Option[GreeksDetails],
                           netPositionBase : NetPositionStatic,
                           netPositionId : String,
                           netPositionView : NetPositionDynamic,
                           singlePosition : Option[SubPositionResponse]
                           ) extends StreamingTopic








