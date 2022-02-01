package finalibre.saxo.rest.outgoing.responses
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath


case class ResponseClient(
                         clientId : String,
                         clientKey : String,
                         defaultAccountId : String,
                         defaultAccountKey : String,
                         defaultCurrency : String,
                         name : String,
                         partnerPlatformId : Option[String]
                         )

object ResponseClient {
  implicit val reads = (
    (JsPath \ "ClientId").read[String] and
    (JsPath \ "ClientKey").read[String] and
    (JsPath \ "DefaultAccountId").read[String] and
    (JsPath \ "DefaultAccountKey").read[String] and
    (JsPath \ "DefaultCurrency").read[String] and
    (JsPath \ "Name").read[String] and
    (JsPath \ "PartnerPlatformId").readNullable[String]
  ) (ResponseClient.apply _)
}