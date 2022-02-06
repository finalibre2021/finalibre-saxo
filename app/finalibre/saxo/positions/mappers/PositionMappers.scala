package finalibre.saxo.positions.mappers

import finalibre.saxo.client.positions.model.PositionDto
import finalibre.saxo.positions.model.Position
import finalibre.saxo.rest.outgoing.responses.ResponsePosition

object PositionMappers {

  implicit class PositionMapper(pos : Position) {
    def toDto = PositionDto(
      pos.positionId,
      pos.accountId,
      pos.assetType,
      pos.status,
      pos.amount,
      pos.currentPrice,
      pos.currentPriceDelayMinutes,
      pos.currentPriceType,
      pos.exposure,
      pos.exposureCurrency,
      pos.exposureInBaseCurrency,
      pos.profitLossOnTrade,
      pos.profitLossOnTradeInBaseCurrency,
      pos.tradeCostsTotal ,
      pos.tradeCostsTotalInBaseCurrency
    )
  }

  implicit class ResponsePositionMapper(resp : ResponsePosition){
    def toModel = Position(
      resp.positionId,
      resp.positionBase.accountId,
      resp.positionBase.assetType,
      resp.positionBase.status,
      resp.positionBase.amount,
      resp.positionView.currentPrice,
      resp.positionView.currentPriceDelayMinutes,
      resp.positionView.currentPriceType,
      resp.positionView.exposure,
      resp.positionView.exposureCurrency,
      resp.positionView.exposureInBaseCurrency,
      resp.positionView.profitLossOnTrade,
      resp.positionView.profitLossOnTradeInBaseCurrency,
      resp.positionView.tradeCostsTotal ,
      resp.positionView.tradeCostsTotalInBaseCurrency
    )
  }

}
