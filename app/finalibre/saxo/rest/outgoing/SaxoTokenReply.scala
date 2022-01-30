package finalibre.saxo.rest.outgoing

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.{Claim, DecodedJWT}

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

  def decryptJWTToken(token : String) = {
    val dec = JWT.decode(token)
    (optString(dec, "access_token"), optLong(dec, "expires_in")) match {
      case (Some(accessToken), Some(expiresIn)) => Some(SaxoTokenReply(accessToken, expiresIn, optString(dec, "refresh_token"), optLong(dec, "refresh_token_expires_in")))
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
