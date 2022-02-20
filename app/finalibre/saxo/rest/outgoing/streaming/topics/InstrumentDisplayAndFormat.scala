package finalibre.saxo.rest.outgoing.streaming.topics

case class InstrumentDisplayAndFormat(
                                       barrierDecimals : Option[Int],
                                       barrierFormat : Option[String],
                                       currency : String,
                                       decimals : Int,
                                       description : String,
                                       displayHint : String,
                                       format : String,
                                       numeratorDecimals : Option[Int],
                                       orderDecimals : Int,
                                       strikeDecimals : Option[Int],
                                       symbol : String,
                                       underlyingInstrumentDescription : Option[String]
                                     )
