package finalibre.saxo.rest.outgoing

import play.api.libs.json.JsonNaming.PascalCase
import play.api.libs.json.{JsonConfiguration, JsonNaming}

object Encoding {
  val DefaultConfiguration = JsonConfiguration(PascalCase)


}
