package finalibre.saxo.rest.outgoing.responses
import finalibre.saxo.rest.outgoing.Encoding
import play.api.libs.functional.syntax._
import play.api.libs.json.JsonNaming.PascalCase
import play.api.libs.json.{JsPath, Json, JsonConfiguration, Reads}

case class ResponseAccount(
                          accountGroupKey : String,
                          accountId : String,
                          accountKey : String,
                          accountSubType : String,
                          accountType : String,
                          active : Boolean,
                          currency : String
                          )

object ResponseAccount {
  implicit val config = Encoding.DefaultConfiguration
  private implicit val responseReads = Json.reads[ResponseAccount]
  private implicit val dataReads : Reads[DataObject[ResponseAccount]] = Json.reads[DataObject[ResponseAccount]]
  implicit val reads : Reads[List[ResponseAccount]] = dataReads.map(accDat => accDat.data.toList)
}
