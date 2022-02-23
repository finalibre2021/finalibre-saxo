package finalibre.saxo.rest.outgoing.streaming.topics

case class FeatureAvailabilityTopic(
                                   feature : String,
                                   available : Boolean
                                   ) extends StreamingTopic
