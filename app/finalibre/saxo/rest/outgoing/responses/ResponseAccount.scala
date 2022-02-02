package finalibre.saxo.rest.outgoing.responses
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

case class ResponseAccount(
                          AccountGroupKey : String,
                          AccountId : String,
                          AccountKey : String,
                          AccountSubType : String,
                          AccountType : String,
                          Active : Boolean,
                          Currency : String
                          )

object ResponseAccount {
  implicit val reads : Reads[ResponseAccount] = Json.reads[ResponseAccount]
  implicit val dataReads : Reads[DataObject[ResponseAccount]] = Json.reads[DataObject[ResponseAccount]]


}
