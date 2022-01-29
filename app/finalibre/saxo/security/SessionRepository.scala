package finalibre.saxo.security

import java.sql.Timestamp

trait SessionRepository {
  def nextId() : String
  def isAuthorized(sessionId : String, ip : String) : Boolean
  def accessTokenFor(sessionId : String, ip : String) : Option[String]
  def nonceExists(sessionId : String, nonce : String) : Boolean
  def initiateAuthenticationProcess(sessionId : String, nonce : String, state : String, forwardUrl : String) : Unit
  def isValidNonceAndState(sessionId : String, nonce : String, state : String) : Boolean
  def finalizeProcess(sessionId : String, nonce : String, saxoAccessToken : String, refreshToken : String, validUntil : Timestamp) : Unit
  def forwardUrlFor(sessionId : String, nonce : String)



}
