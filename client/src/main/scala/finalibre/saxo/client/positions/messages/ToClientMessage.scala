package finalibre.saxo.client.positions.messages

import finalibre.saxo.client.positions.model.{ClientDto, PositionDto}

case class ToClientMessage(
                          clients : Seq[ClientDto] = Nil,
                          positions : Seq[PositionDto] = Nil
                          )
