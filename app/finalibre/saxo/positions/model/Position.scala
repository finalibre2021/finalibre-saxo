package finalibre.saxo.positions.model

import java.time.{LocalDate, LocalDateTime}

case class Position(
                   positionId : String,
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
