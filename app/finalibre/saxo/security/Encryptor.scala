package finalibre.saxo.security

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.Claim

import java.security.MessageDigest
import java.time.{Instant, LocalDateTime}
import java.util.{Base64, TimeZone}
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import scala.jdk.CollectionConverters._

class Encryptor @Inject()(secret: ApplicationSecret) {
  private lazy val EntrySplitString = "!!==!!"
  private lazy val PairSplitString = "==!!=="
  private lazy val CipherName = "AES/ECB/PKCS5Padding"

  private lazy val key = java.util.Arrays.copyOf(careateMessageDigest().digest(secret.secretKey.getBytes), 16)

  private def createCipher() = Cipher.getInstance(CipherName)
  private def careateMessageDigest() = MessageDigest.getInstance("SHA-1")
  private def createKeySpec() = new SecretKeySpec(key, "AES")


  def encrypt(str : String) : String = {
    val cipher = createCipher()
    val keySpec = createKeySpec()
    cipher.init(Cipher.ENCRYPT_MODE, keySpec)
    new String(Base64.getEncoder.encode(cipher.doFinal(str.getBytes("UTF-8"))), "UTF-8")
  }

  def decrypt(str: String): String = {
    val cipher = createCipher()
    val keySpec = createKeySpec()
    cipher.init(Cipher.DECRYPT_MODE, keySpec)
    new String(cipher.doFinal(Base64.getDecoder.decode(str)), "UTF-8")
  }

  def serializeAndEncrypt(map : List[(String, String)]) : String = {
    val inputString = map.map(en => en._1 + PairSplitString + en._2).mkString(EntrySplitString)
    encrypt(inputString)
  }

  def decryptAndDeserialize(str : String) = {
    val decrypted = decrypt(str)
    decrypted.split(EntrySplitString).toList.map(_.split(PairSplitString)).filter(_.size == 2).map(en => en(0) -> en(1))
  }

  def decryptJWTToken(token : String) = {
    val dec = JWT.decode(token)
    JWTToken(
      dec.getIssuer,
      dec.getAudience.asScala.toList,
      dec.getSubject,
      optClaim(dec.getClaim("email")).map(_.asString),
      optClaim(dec.getClaim("email_verified")).map(_.asBoolean),
      asLocalDateTime(dec.getClaim("iat").asLong),
      asLocalDateTime(dec.getClaim("exp").asLong),
      optClaim(dec.getClaim("nonce")).map(_.asString)
    )
  }

  def optClaim(cl : Claim) : Option[Claim] = if(cl.isNull) None else Some(cl)
  def asLocalDateTime(longVal : Long) = LocalDateTime.ofInstant(Instant.ofEpochMilli(longVal * 1000L), TimeZone.getDefault.toZoneId)



  case class JWTToken(issuer : String, audience : Seq[String], subject : String, email : Option[String], emailVerified : Option[Boolean], issuedAt : LocalDateTime, expires : LocalDateTime, nonce : Option[String])


}
