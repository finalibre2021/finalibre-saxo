package finalibre.saxo.rest.outgoing.streaming.topics

case class SubPositionResponse(
                                positionId : String,
                                positionBase : Option[SubPositionBase],
                                positionView : Option[SubPositionView]
                              )
