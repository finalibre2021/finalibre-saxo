package finalibre.saxo.security.db.postgres

import finalibre.saxo.security.SessionRepository
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import java.sql.Timestamp
import javax.inject.Inject
import scala.concurrent.Await

class PostgresSessionRepository @Inject() (db : Database) extends SessionRepository {

  override def nextId(): String = {
    PostgresSessionRepository.idLock.synchronized {
      var returnee : Option[String] = None
      while(returnee.isEmpty) {
        val uuidQuery = sql"select gen_random_uuid()::text".as[String]
        val uuid = Await.result(db.run(uuidQuery), 10.seconds ).head
        val exists = Await.result(db.run(Tables.sessions.filter(en => en.sessionId === uuid).result), 10.seconds).headOption.isDefined
        if(!exists)
          returnee = Some(uuid)
      }
      return returnee.head
    }
  }

  override def isAuthorized(sessionId: String, ip: String): Boolean = Await.result(
    db.run(
      (Tables.sessions.filter(sess => sess.sessionId === sessionId && sess.ip === ip) join
        Tables.processes.filter(proc => proc.saxoAccessToken.isDefined && proc.validUntil > new Timestamp(System.currentTimeMillis())) on {_.sessionId === _.sessionId}
        ).map(p => p._1.sessionId).result
    ),
    10.seconds
  ).headOption.isDefined

  override def accessTokenFor(sessionId: String, ip: String): Option[String] = ???

  override def nonceExists(sessionId: String, nonce: String): Boolean = ???

  override def initiateAuthenticationProcess(sessionId: String, nonce: String, state: String, forwardUrl: String): Unit = ???

  override def isValidNonceAndState(sessionId: String, nonce: String, state: String): Boolean = ???

  override def finalizeProcess(sessionId: String, nonce: String, saxoAccessToken: String, refreshToken: String, validUntil: Timestamp): Unit = ???

  override def forwardUrlFor(sessionId: String, nonce: String): Unit = ???

  def createTables(): Unit = {

  }
}

object PostgresSessionRepository {
  private val idLock = new {}
}
