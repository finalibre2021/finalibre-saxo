package finalibre.saxo.security.model

import java.sql.Timestamp

case class UserSession(
                        sessionId : String,
                        ip : String,
                        started : Timestamp
                      )
