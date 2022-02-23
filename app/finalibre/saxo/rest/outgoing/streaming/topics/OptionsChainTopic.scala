package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class OptionsChainTopic(
                            assetType : Option[String],
                            expiries : Option[Seq[ExpiryData]],
                            expiryCount : Option[Int],
                            lastUpdated : Option[LocalDateTime]
                            ) extends StreamingTopic
