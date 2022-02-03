package finalibre.saxo.rest.outgoing.responses
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads}

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
  private implicit val casedReads : Reads[CasedResponseAccount] = Json.reads[CasedResponseAccount]
  private implicit val dataReads : Reads[DataObject[CasedResponseAccount]] = Json.reads[DataObject[CasedResponseAccount]]
  implicit val reads : Reads[List[ResponseAccount]] = dataReads.map(datAcc => datAcc.Data.toList.map(
    acc => ResponseAccount(acc.AccountGroupKey, acc.AccountId, acc.AccountKey, acc.AccountSubType, acc.AccountType, acc.Active, acc.Currency)
  ))



  private case class CasedResponseAccount(
                                           AccountGroupKey : String,AccountId : String,AccountKey : String,AccountSubType : String,
                                           AccountType : String, Active : Boolean, Currency : String
                                         )


}
