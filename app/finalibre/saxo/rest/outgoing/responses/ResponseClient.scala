package finalibre.saxo.rest.outgoing.responses
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}
import finalibre.saxo.rest.outgoing.Encoding


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
  implicit val config = Encoding.DefaultConfiguration
  implicit val reads = Json.reads[ResponseClient]
  private implicit val dataReads : Reads[DataObject[ResponseClient]] = Json.reads[DataObject[ResponseClient]]
  implicit val listReads : Reads[List[ResponseClient]] = dataReads.map(accDat => accDat.data.toList)
}