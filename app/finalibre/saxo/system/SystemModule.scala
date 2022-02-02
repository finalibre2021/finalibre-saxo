package finalibre.saxo.system

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.security.{ApplicationSecret, Encryptor, SessionRepository}
import finalibre.saxo.security.db.postgres.PostgresSessionRepository
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class SystemModule (environment: Environment, config : Configuration) extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    List(
      bind[SaxoScheduler].toSelf.eagerly()
    )
  }
}