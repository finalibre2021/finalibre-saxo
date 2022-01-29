package finalibre.saxo.security.db.postgres

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.security.SessionRepository
import finalibre.saxo.security.model.{AuthenticationProcess, UserSession}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{Await, ExecutionContext}

class PostgresSessionRepository @Inject() (context : ExecutionContext) extends SessionRepository {
  private val dbConf = SaxoConfig.Persistance.applicationDb
  private val dataSource = new HikariDataSource(
    new HikariConfig {
      setJdbcUrl(dbConf.user)
      setUsername(dbConf.user)
      setPassword(dbConf.password)
      setMaximumPoolSize(dbConf.noOfParallel)
    }
  )
  private val db = Database.forDataSource(dataSource, Some(dbConf.noOfParallel))
  private implicit val shortDuration = 5.seconds
  private def now = new Timestamp(System.currentTimeMillis())
  private implicit val executionContext = context

  override def nextIdAndNonce() : (String,String) = {
    PostgresSessionRepository.idLock.synchronized {
      var sessionId : Option[String] = None
      while(sessionId.isEmpty) {
        val uuidQuery = sql"select gen_random_uuid()::text".as[String]
        val uuid = Await.result(db.run(uuidQuery), shortDuration ).head
        val exists = Await.result(db.run(Tables.sessions.filter(en => en.sessionId === uuid).result), 10.seconds).headOption.isDefined
        if(!exists)
          sessionId = Some(uuid)
      }
      return (sessionId.head, UUID.randomUUID().toString())
    }
  }

  override def isAuthorized(sessionId: String, ip: String): Boolean = Await.result(
    db.run(
      (Tables.sessions.filter(sess => sess.sessionId === sessionId && sess.ip === ip) join
        Tables.processes.filter(proc => proc.saxoAccessToken.isDefined && proc.validUntil > now) on {_.sessionId === _.sessionId}
        ).map(p => p._1.sessionId).result
    ),
    shortDuration
  ).headOption.isDefined

  override def accessTokenFor(sessionId: String, ip: String): Option[String] = Await.result(
    db.run(
      (Tables.sessions.filter(sess => sess.sessionId === sessionId && sess.ip === ip) join
        Tables.processes.filter(proc => proc.validUntil > now) on {_.sessionId === _.sessionId}
        ).map(p => p._2.saxoAccessToken).result
    ),
    shortDuration
  ).headOption.flatten

  override def nonceExists(sessionId: String, nonce: String): Boolean = Await.result(
    db.run(
      (Tables.sessions.filter(sess => sess.sessionId === sessionId) join
        Tables.processes.filter(proc => proc.nonce === nonce) on {_.sessionId === _.sessionId}
        ).map(p => p._2.nonce).result
    ),
    shortDuration
  ).headOption.isDefined

  override def initiateAuthenticationProcess(sessionId: String, ip : String, nonce: String, state: String, forwardUrl: String): Unit = {
    val userSessionToInsert = UserSession(sessionId, ip, new Timestamp(System.currentTimeMillis()))
    val processToInsert = AuthenticationProcess(sessionId, nonce, state, forwardUrl, None, None, None)
    val action = db.run(Tables.sessions += userSessionToInsert).flatMap(_ => db.run(Tables.processes += processToInsert))
    Await.result(action, shortDuration * 2)
  }

  override def isValidNonceAndState(sessionId: String, nonce: String, state: String): Boolean = Await.result(
    db.run(
      (Tables.sessions.filter(sess => sess.sessionId === sessionId) join
        Tables.processes.filter(proc => proc.nonce === nonce && proc.state === state) on {_.sessionId === _.sessionId}
        ).map(p => p._2.nonce).result
    ),
    shortDuration
  ).headOption.isDefined

  override def updateSaxoTokenData(sessionId: String, nonce: String, saxoAccessToken: String, refreshToken: String, validUntil: Timestamp): Unit = Await.result(
    db.run(
      Tables.processes
        .filter(proc => proc.sessionId === sessionId && proc.nonce === nonce)
        .map(proc => (proc.saxoAccessToken, proc.saxoRefreshToken, proc.validUntil))
        .update((Some(saxoAccessToken), Some(refreshToken), Some(validUntil)))
    ),
    shortDuration
  )

  override def forwardUrlFor(sessionId: String, nonce: String): Option[String] = Await.result(
    db.run(
      Tables.processes
        .filter(proc => proc.sessionId === sessionId && proc.nonce === nonce)
        .map(proc => proc.forwardUrl)
        .result
    ),
    shortDuration
  ).headOption

  def createTables(): Unit = {

  }
}

object PostgresSessionRepository {
  private val idLock = new {}
}
