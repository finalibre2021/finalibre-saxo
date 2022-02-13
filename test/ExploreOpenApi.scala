import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.{OpenApiCallingContext, OpenApiService}
import finalibre.saxo.security.db.postgres.{PostgresSessionRepository, Tables}
import play.api.libs.ws.ahc.AhcWSClient

import java.io.File
import java.sql.Timestamp
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.io.Source
import slick.jdbc.PostgresProfile.api._

object ExploreOpenApi {

  val ClientKey = "sFwk2IG|p22o9vmB2GEgoA=="

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = Materializer.matFromSystem
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val dt = 1.minute
    implicit val client = AhcWSClient()
    implicit val openApiClient = new OpenApiService(client, executionContext)
    implicit val sessionRepository = new PostgresSessionRepository(executionContext)

    val repo = new PostgresSessionRepository(executionContext)
    val db = repo.db
    val now = new Timestamp(System.currentTimeMillis())
    val (sessionId, accessTokenOpt) = ("1234", Some(loadTempToken)) /*Await.result(
      db.run(Tables.processes
        .filter(proc => proc.saxoAccessToken.isDefined && proc.saxoAccessToken.isDefined && proc.validUntil > now)
        .result
      ), 1.minute
    ).sortBy(p => -p.refreshValidUntil.get.getTime)
      .headOption
      .map(proc => (proc.sessionId, proc.saxoAccessToken))
      .head*/

    implicit val contx = OpenApiCallingContext(sessionId, accessTokenOpt.get)
    OpenApiCallingContext.call((api, cntx) => api.apiDescription("port", cntx)).foreach {
      case Left(err) => println(err.errorString)
      case Right(resp) => {
        println(resp.body)
      }
    }


    Await.result(system.terminate(), 10.seconds)


  }

  def loadTempToken = {
    val file = new File("""c:\git\finalibre-saxo\conf\temp-token.txt""")
    println(s"Using file: ${file.getAbsolutePath}")
    Source.fromFile(file).getLines().toList.head
  }


}



