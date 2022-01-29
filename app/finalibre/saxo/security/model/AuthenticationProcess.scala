package finalibre.saxo.security.model

import java.sql.Timestamp

case class AuthenticationProcess(
                                  sessionId : String,
                                  nonce : String,
                                  state : String,
                                  forwardUrl : String,
                                  saxoAccessToken : Option[String],
                                  saxoRefreshToken : Option[String],
                                  validUntil : Option[Timestamp]
                                )
