package finalibre.saxo.security

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.SaxoAuthenticator
import finalibre.saxo.security.db.postgres.PostgresSessionRepository
import play.api.{Configuration, Environment}
import play.api.inject._

class SecurityModule(environment: Environment, config : Configuration) extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    List(
      bind[ApplicationSecret].toInstance(SaxoConfig.Security.applicationSecret),
      bind[Encryptor].toSelf,
      bind[SessionRepository].to[PostgresSessionRepository],
      bind[SaxoAuthenticator].toSelf
    )

  }
}
