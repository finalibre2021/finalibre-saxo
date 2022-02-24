package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class AccountTopic(
                       data : Seq[AccountEntry]
                       ) extends StreamingTopic


