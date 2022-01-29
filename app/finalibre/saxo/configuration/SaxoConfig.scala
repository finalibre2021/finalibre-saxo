package finalibre.saxo.configuration

import com.typesafe.config.ConfigFactory
import finalibre.saxo.security.ApplicationSecret

object SaxoConfig {
  private lazy val conf = ConfigFactory
    .load("application.conf")
    .withFallback(ConfigFactory.load())


  object Security {
    private lazy val secretKey = conf.getString("play.http.secret.key")
    lazy val applicationSecret = ApplicationSecret(secretKey)
  }

  object Persistance {
    private lazy val dbConf = conf.getConfig("finalibre.saxo.db")
    lazy val applicationDb = ConnectionDetails(dbConf.getString("url"), dbConf.getString("user"), dbConf.getString("password"), dbConf.getInt("no-of-threads"))

    case class ConnectionDetails(url : String, user : String, password : String, noOfParallel : Int )
  }


}
