package finalibre.saxo.configuration

import com.typesafe.config.ConfigFactory
import finalibre.saxo.security.ApplicationSecret

object SaxoConfig {
  private lazy val conf = ConfigFactory
    .load("application.conf")
    .withFallback(ConfigFactory.load())
  private lazy val saxoConf = conf.getConfig("finalibre.saxo")

  lazy val tokenLockLifeSpanInMinutes = saxoConf.getInt("token-lock-lifespan-minutes")
  lazy val tokenRefreshIntervalInMinutes = saxoConf.getInt("token-refresh-interval-minutes")


  object Security {
    private lazy val secretKey = conf.getString("play.http.secret.key")
    lazy val applicationSecret = ApplicationSecret(secretKey)
  }

  object Persistance {
    private lazy val dbConf = saxoConf.getConfig("db")
    lazy val applicationDb = ConnectionDetails(dbConf.getString("url"), dbConf.getString("user"), dbConf.getString("password"), dbConf.getInt("no-of-threads"))

    case class ConnectionDetails(url : String, user : String, password : String, noOfParallel : Int )
  }

  object Rest {
    object Outgoing {
      private lazy val outConf = saxoConf.getConfig("rest.outgoing")
      lazy val openApiBaseUrl = outConf.getString("open-api-base-url")
      lazy val authenticationBaseUrl = outConf.getString("authentication-base-url")
      lazy val clientId = outConf.getString("client-id")
      lazy val clientSecret = outConf.getString("client-secret")
    }

  }


}
