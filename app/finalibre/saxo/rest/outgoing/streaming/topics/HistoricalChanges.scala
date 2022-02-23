package finalibre.saxo.rest.outgoing.streaming.topics

case class HistoricalChanges(
                              fiftyTwoWeekHigh : Double,
                              fiftyTwoWeekLow : Double,
                              percentChange1Month : Double,
                              percentChange1Year : Double,
                              percentChange2Months : Double,
                              percentChange2Years : Double,
                              percentChange3Months : Double,
                              percentChange3Years : Double,
                              percentChange5Years : Double,
                              percentChange6Months : Double,
                              percentChangeDaily : Double,
                              percentChangeWeekly : Double
                            )
