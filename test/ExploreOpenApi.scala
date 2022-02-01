import akka.actor.ActorSystem
import akka.stream.Materializer
import finalibre.saxo.rest.outgoing.OpenApiService
import finalibre.saxo.security.db.postgres.{PostgresSessionRepository, Tables}
import play.api.libs.ws.ahc.AhcWSClient

import java.io.File
import java.sql.Timestamp
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.io.Source

object ExploreOpenApi {
  def main(args: Array[String]): Unit = {
    implicit val system  = ActorSystem()
    implicit val mat = Materializer.matFromSystem
    implicit val executionContext : ExecutionContext = system.dispatcher
    implicit val dt = 1.minute
    implicit val client = AhcWSClient()
    val openApiClient = new OpenApiService(client, executionContext)

    import slick.jdbc.PostgresProfile.api._
    val repo = new PostgresSessionRepository(executionContext)
    val db = repo.db
    val now = new Timestamp(System.currentTimeMillis())
    val latestValidTokenWithRefresh = Await.result(
      db.run(Tables.processes
        .filter(proc => proc.saxoAccessToken.isDefined && proc.saxoRefreshToken.isDefined && proc.refreshValidUntil > now)
        .result
    ), 1.minute
    ).sortBy(p => - p.refreshValidUntil.get.getTime).headOption

    latestValidTokenWithRefresh.foreach {
      proc => {
        openApiClient.refreshToken(proc.saxoRefreshToken.get, proc.saxoAccessToken.get).foreach {
          case Left(err) => println(s"Error, error, error: $err")
          case Right(resp) => {
            println(s"Success: refresh token: ${proc.saxoRefreshToken.get}")
            println(s"   exchanged for access token: ${resp.accessToken}")
            println(s"   and refresh token: ${resp.refreshToken.getOrElse("")}")

          }
        }
      }
    }

  }


}