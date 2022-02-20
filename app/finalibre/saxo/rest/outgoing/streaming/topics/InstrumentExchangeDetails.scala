package finalibre.saxo.rest.outgoing.streaming.topics

case class InstrumentExchangeDetails(
                                      description : String,
                                      exchangeId : String,
                                      isOpen : Boolean,
                                      timeZoneId : String
                                    )
