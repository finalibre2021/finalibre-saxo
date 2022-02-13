package finalibre.saxo.system

import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}


class SystemModule (environment: Environment, config : Configuration) extends Module {

  override def bindings(environment: Environment, configuration: Configuration): collection.Seq[Binding[_]] = {
    List(
      bind[SaxoScheduler].toSelf.eagerly()
    )
  }
}