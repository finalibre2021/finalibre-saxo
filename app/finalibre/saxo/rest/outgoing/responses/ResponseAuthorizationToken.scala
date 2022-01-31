package finalibre.saxo.rest.outgoing.responses

case class ResponseAuthorizationToken(
                                       accessToken : String,
                                       expiresIn : Long,
                                       refreshToken : Option[String],
                                       refreshExpiresIn : Option[Long]
                                     )
