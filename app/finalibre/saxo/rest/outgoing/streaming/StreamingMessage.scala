package finalibre.saxo.rest.outgoing.streaming

import finalibre.saxo.rest.outgoing.streaming.StreamingMessage.PayloadType.PayloadType

private object StreamingMessage {
  object PayloadType extends Enumeration {
    type PayloadType = Value
    val Json, ProtoBuf = Value
  }
}

private case class StreamingMessage(messageId : Long, referenceId : String, payloadType: PayloadType, payload : String){
}

