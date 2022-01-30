package finalibre.saxo.rest.outgoing

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.{Claim, DecodedJWT}
import play.api.libs.json.Json

import java.time.{Instant, LocalDateTime}
import java.util.TimeZone

case class SaxoTokenReply(
                     accessToken : String,
                     expiresIn : Long,
                     refreshToken : Option[String],
                     refreshExpiresIn : Option[Long]
                   ) {
  val expiresAt = LocalDateTime.now().plusSeconds(expiresIn)
  val refreshExpiresAt = refreshExpiresIn.map(ref =>  LocalDateTime.now().plusSeconds(ref))
}

object SaxoTokenReply {
  private val numberRegex = "^[0-9]+$".r

  def parseSaxoReplyBody(body : String) = {
    val json = Json.parse(body)
    val accessToken = (json \ "access_token").asOpt[String]
    val expiresIn = (json \ "expires_in").asOpt[Long]
    val refreshAccessToken = (json \ "refresh_token").asOpt[String]
    val refreshExpiresIn = (json \ "refresh_token_expires_in").asOpt[Long]

    (accessToken, expiresIn) match {
      case (Some(accTok), Some(expIn)) => Some(SaxoTokenReply(accTok, expIn, refreshAccessToken, refreshExpiresIn))
      case _ => None

    }

  }

  def optClaim(dec : DecodedJWT, name : String) : Option[Claim] = dec.getClaim(name) match {
    case null => None
    case cl if cl.isNull => None
    case cl => Some(cl)
  }

  def optString(dec : DecodedJWT, name : String) : Option[String] =
    optClaim(dec,name)
      .map(cl => cl.asString())


  def optLong(dec : DecodedJWT, name : String) : Option[Long] =
    optClaim(dec,name)
      .map(cl => cl.asString())
      .filter(cl => numberRegex.matches(cl))
      .map(cl => cl.toLong)


}
