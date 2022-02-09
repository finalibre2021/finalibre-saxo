package finalibre.saxo.client.positions.messages

import finalibre.saxo.client.positions.model.{ClientDto, PositionDto}

case class ToClientMessage(
                          clients : Option[Seq[ClientDto]] = None,
                          positions : Option[Seq[PositionDto]] = None
                          )
