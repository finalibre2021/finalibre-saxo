package finalibre.saxo.client.positions.model

case class PositionDto(
                        positionId : String,
                        clientId : String,
                        accountId : String,
                        assetType: String,
                        status: String,
                        amount : Double,
                        currentPrice: Double,
                        currentPriceDelayMinutes: Long,
                        currentPriceType: String,
                        exposure: Double,
                        exposureCurrency: String,
                        exposureInBaseCurrency: Double,
                        profitLossOnTrade: Double,
                        profitLossOnTradeInBaseCurrency: Double,
                        tradeCostsTotal: Double,
                        tradeCostsTotalInBaseCurrency: Double
                      )
