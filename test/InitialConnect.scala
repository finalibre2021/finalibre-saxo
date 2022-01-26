import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.ws.ahc.AhcWSClient

import java.io.File
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.io.Source

object InitialConnect {

  def main(args: Array[String]): Unit = {
    implicit val system  = ActorSystem()
    implicit val mat = Materializer.matFromSystem
    implicit val executionContext : ExecutionContext = system.dispatcher
    implicit val dt = 1.minute
    implicit val client = AhcWSClient()

    var tempToken = Source.fromFile(new File("conf/temp-token.txt"), "UTF-8").getLines().mkString("")
    var respFuture = client
      .url("https://gateway.saxobank.com/sim/openapi/port/v1/users/me")
      .withHttpHeaders(
        "Accept"-> "*/*",
        "Authorization" -> s"BEARER $tempToken",
        "Content-Type" -> "application/json"
      ).get()
    respFuture.map {
      case resp => {
        var jsValue = resp.json
        println("JsValue: " + jsValue)

      }
    }


    //system.terminate().wait()


  }



}
