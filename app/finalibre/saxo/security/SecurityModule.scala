package finalibre.saxo.security

import finalibre.saxo.configuration.SaxoConfig
import play.api.{Configuration, Environment}
import play.api.inject._

class SecurityModule(environment: Environment, config : Configuration) extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    List(
      bind[ApplicationSecret].toInstance(SaxoConfig.Security.applicationSecret),
      bind[Encryptor].toSelf
    )

  }
}
