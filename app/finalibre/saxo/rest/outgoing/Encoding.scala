package finalibre.saxo.rest.outgoing

import play.api.libs.json.JsonNaming.PascalCase
import play.api.libs.json.{JsonConfiguration, JsonNaming}

object Encoding {
  val DefaultConfiguration = JsonConfiguration(PascalCase)

  class PascalCaseEncoder extends ai.x.play.json.BaseNameEncoder {
    override def encode(str : String) : String = str.head.toUpper + str.drop(1)
  }

  val PlayJsonXPascalEncoder = new PascalCaseEncoder()

}
