package finalibre.saxo.rest.outgoing.responses
import play.api.libs.functional.syntax._
import play.api.libs.json.JsPath

import java.time.LocalDateTime

case class ResponseAuthorizationToken(
                                       accessToken : String,
                                       expiresIn : Long,
                                       refreshToken : Option[String],
                                       refreshExpiresIn : Option[Long]
                                     ) {
  val expiresAt = LocalDateTime.now().plusSeconds(expiresIn)
  val refreshExpiresAt = refreshExpiresIn.map(ref =>  LocalDateTime.now().plusSeconds(ref))
}

object ResponseAuthorizationToken {
  implicit val reads = (
    (JsPath \ "access_token").read[String] and
      (JsPath \ "expires_in").read[Long] and
      (JsPath \ "refresh_token").readNullable[String] and
      (JsPath \ "refresh_token_expires_in").readNullable[Long]
  )(ResponseAuthorizationToken.apply _)
}
