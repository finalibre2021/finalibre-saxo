package finalibre.saxo.rest.outgoing.streaming.topics

case class FixedIncomeData(
                            closedAccruedInterest : Double,
                            closedAccruedInterestInBaseCurrency : Double,
                            openAccruedInterest : Double,
                            openAccruedInterestInBaseCurrency : Double
                          )
