package finalibre.saxo.rest.outgoing.streaming.topics

case class InitialMargin(
                          collateralAvailable : Double,
                          marginAvailable : Double,
                          marginUsedByCurrentPositions : Double,
                          marginUtilizationPct : Double,
                          netEquityForMargin : Double,
                          otherCollateralDeduction : Double
                        )
