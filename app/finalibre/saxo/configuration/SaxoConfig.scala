package finalibre.saxo.configuration

import com.typesafe.config.ConfigFactory
import finalibre.saxo.security.ApplicationSecret

object SaxoConfig {
  private lazy val conf = ConfigFactory
    .load("production.conf")
    .withFallback(ConfigFactory.load())

  object Security {
    private lazy val secretKey = conf.getString("play.http.secret.key")
    lazy val applicationSecret = ApplicationSecret(secretKey)
  }


}
