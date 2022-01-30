package finalibre.saxo.security.db.postgres

import finalibre.saxo.security.model.{AuthenticationProcess, UserSession}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp

object Tables {
  val sessions = TableQuery[UserSessionTable]
  val processes = TableQuery[AuthenticationProcessTable]

  val schema = sessions.schema ++ processes.schema

  class UserSessionTable(tag : Tag) extends Table[UserSession](tag, "SECURITY_USERSESSION"){
    def sessionId = column[String]("SESSIONID", O.PrimaryKey)
    def ip = column[String]("IPADDRESS")
    def started = column[Timestamp]("STARTED")

    def * = (sessionId, ip, started) <> (UserSession.tupled, UserSession.unapply)
  }

  class AuthenticationProcessTable(tag : Tag) extends Table[AuthenticationProcess](tag, "SECURITY_AUTHPROCESS") {
    def sessionId = column[String]("SESSIONID")
    def nonce = column[String]("SIGNINNONCE")
    def state = column[String]("SIGNINSTATE")
    def forwardUrl = column[String]("FORWARDURL", O.Length(2000))
    def saxoAccessToken = column[Option[String]]("ACCESSTOKEN")
    def validUntil = column[Option[Timestamp]]("VALIDUNTIL")
    def saxoRefreshToken = column[Option[String]]("REFRESHTOKEN")
    def refreshValidUntil = column[Option[Timestamp]]("REFRESHVALIDUNTIL")

    def * = (sessionId, nonce, state, forwardUrl, saxoAccessToken, validUntil, saxoRefreshToken, refreshValidUntil) <> (AuthenticationProcess.tupled, AuthenticationProcess.unapply)

    def pk = primaryKey("PK_SECURITY_AUTHPROCESS", (sessionId, nonce))
    def fkSessionId = foreignKey("FK_AUTHPROCESS_SESSID", sessionId, sessions)(_.sessionId, onDelete = ForeignKeyAction.Cascade)

  }


}
