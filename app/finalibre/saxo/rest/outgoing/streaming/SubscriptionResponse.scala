package finalibre.saxo.rest.outgoing.streaming

import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic

sealed trait SubscriptionResponse[T <: StreamingTopic]

case class SingleEntrySubscriptionResponse[T <: StreamingTopic](
                               contextId : String,
                               format : Option[String],
                               inactivityTimeout : Option[Int],
                               referenceId : String,
                               refreshRate : Option[Int],
                               snapshot : T
                               ) extends SubscriptionResponse[T]

case class MultiEntrySubscriptionResponse[T <: StreamingTopic](
                                                                 contextId : String,
                                                                 format : Option[String],
                                                                 inactivityTimeout : Option[Int],
                                                                 referenceId : String,
                                                                 refreshRate : Option[Int],
                                                                 snapshot : Seq[T]
                                                               ) extends SubscriptionResponse[T]
