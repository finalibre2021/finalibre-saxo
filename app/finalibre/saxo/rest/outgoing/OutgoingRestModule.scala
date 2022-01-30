package finalibre.saxo.rest.outgoing

import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class OutgoingRestModule (environment: Environment, config : Configuration) extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    List(
      bind[OpenApiService].toSelf
    )

  }
}