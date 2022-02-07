package finalibre.saxo.positions

import finalibre.saxo.positions.RestPositionDataLoader.OpenApiCallingContext
import finalibre.saxo.positions.model.{Account, Client, Position}
import finalibre.saxo.rest.outgoing.OpenApiService

import scala.concurrent.{ExecutionContext, Future}
import finalibre.saxo.positions.mappers.ClientMappers._
import finalibre.saxo.positions.mappers.PositionMappers._

class RestPositionDataLoader(executionContext: ExecutionContext) extends PositionDataLoader {
  implicit val execContext = executionContext

  override def loadClients(token : String)(implicit callingContext : OpenApiCallingContext[]) : Future[Either[String, Seq[Client]]] = apiService.clients()(token).map {
    case Right(value) => Right(value.map(_.toModel))
    case Left(err) => Left(err.errorString)
  }
  override def loadPositions(clientKey : String, token : String) : Future[Either[String, Seq[Position]]] = apiService.positions(None, None, clientKey)(token).map {
    case Right(value) => Right(samplePositionsFor(clientKey))
    case Left(err) => Left(err.errorString)
  }

  def samplePositionsFor(accountId : String) : Seq[Position] = List(
    Position(
      "KR7005930003",
      accountId,
      "Stock",
      "Valid",
      10_234d,
      192.02d,
      10L,
      "Bid",
      10_234d * 192.02d * 0.0054d,
      "KRW",
      10_234d * 192.02d,
      24_204.23d * 0.0054d,
      24_204.23d,
      263.23d * 0.0054d,
      263.23d
    ),
    Position(
      "US0970231058 ",
      accountId,
      "Stock",
      "Valid",
      103_134d,
      4_922.17d,
      10L,
      "Bid",
      103_134d * 4_922.17d * 6.5d,
      "USD",
      103_134d * 4_922.17d,
      - 5_756.96d * 6.5d,
      - 5_756.96d,
      263.23d * 6.5d,
      263.23d
    )


  )
}

object RestPositionDataLoader {
  type OpenApiCallingContext[A] = (OpenApiService => Future[A]) => Future[A]
}
