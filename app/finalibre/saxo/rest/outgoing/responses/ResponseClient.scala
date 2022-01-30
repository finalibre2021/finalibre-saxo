package finalibre.saxo.rest.outgoing.responses

case class ResponseClient(
                         clientId : String,
                         clientKey : String,
                         defaultAccountId : String,
                         defaultAccountKey : String,
                         defaultCurrency : String,
                         name : String,
                         partnerPlatformId : Option[String]
                         )
