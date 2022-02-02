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
import slick.jdbc.PostgresProfile.api._

object ExploreOpenApi {

  val ClientKey = "sFwk2IG|p22o9vmB2GEgoA=="

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = Materializer.matFromSystem
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val dt = 1.minute
    implicit val client = AhcWSClient()
    val openApiClient = new OpenApiService(client, executionContext)

    val repo = new PostgresSessionRepository(executionContext)
    val db = repo.db
    val now = new Timestamp(System.currentTimeMillis())
    //refreshToken(openApiClient, db, repo)
    val latestValidToken = Await.result(
      db.run(Tables.processes
        .filter(proc => proc.saxoAccessToken.isDefined && proc.saxoAccessToken.isDefined && proc.validUntil > now)
        .result
      ), 1.minute
    ).sortBy(p => -p.refreshValidUntil.get.getTime)
      .headOption
      .flatMap(_.saxoAccessToken)

    println(latestValidToken)

    latestValidToken.foreach {
      case token => {
        println(s"Got token: $token")
        openApiClient.accounts(ClientKey)(token).foreach {
          case resp => resp match {
            case Right(res) => {
              println("Got response")
              res.foreach(acc => println(acc))
            }
            case Left(err) => println(s"Error: $err")
          }

        }
      }
    }
    Await.result(system.terminate(), 10.seconds)


  }


  def refreshToken(openApiService: OpenApiService, db: Database, repo: PostgresSessionRepository)(implicit executionContext: ExecutionContext): Unit = {
    val now = new Timestamp(System.currentTimeMillis())
    val latestValidTokenWithRefresh = Await.result(
      db.run(Tables.processes
        .filter(proc => proc.saxoAccessToken.isDefined && proc.saxoRefreshToken.isDefined && proc.refreshValidUntil > now)
        .result
      ), 1.minute
    ).sortBy(p => -p.refreshValidUntil.get.getTime).headOption

    latestValidTokenWithRefresh.foreach {
      proc => {
        openApiService.refreshToken(proc.saxoRefreshToken.get).foreach {
          case Left(err) => println(s"Error, error, error: $err")
          case Right(resp) => {
            proc.saxoAccessToken.foreach {
              case curToken => {
                repo.updateSaxoTokenDataByCurrentToken(proc.sessionId, curToken, resp.accessToken, resp.expiresAt, resp.refreshToken, resp.refreshExpiresAt)
                println(s"Success: refresh token: ${proc.saxoRefreshToken.get}")
                println(s"   exchanged for access token: ${resp.accessToken}")
                println(s"   and refresh token: ${resp.refreshToken.getOrElse("")}")
              }
            }
          }
        }

      }
    }
  }
}



