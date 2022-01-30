package finalibre.saxo.security

import java.sql.Timestamp
import java.time.LocalDateTime

trait SessionRepository {
  def nextIdAndNonce() : (String,String)
  def isAuthorized(sessionId : String, ip : String) : Boolean
  def accessTokenFor(sessionId : String, ip : String) : Option[String]
  def nonceExists(sessionId : String, nonce : String) : Boolean
  def initiateAuthenticationProcess(sessionId : String, ip : String, nonce : String, state : String, forwardUrl : String) : Unit
  def isValidNonceAndState(sessionId : String, nonce : String, state : String) : Boolean
  def liveSaxoToken(sessionId : String) : Option[String]
  def updateSaxoTokenData(sessionId : String, nonce : String, saxoAccessToken : String, validUntil : LocalDateTime, refreshToken : Option[String], refreshValidUntil : Option[LocalDateTime]) : Unit
  def forwardUrlFor(sessionId : String, nonce : String) : Option[String]



}
