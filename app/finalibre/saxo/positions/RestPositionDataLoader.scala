package finalibre.saxo.positions

import finalibre.saxo.positions.model.{Account, Client, Position}
import finalibre.saxo.rest.outgoing.{OpenApiCallingContext, OpenApiService}

import scala.concurrent.{ExecutionContext, Future}
import finalibre.saxo.positions.mappers.ClientMappers._
import finalibre.saxo.positions.mappers.PositionMappers._
import finalibre.saxo.security.SessionRepository

private class RestPositionDataLoader(sessionId : String, openApiService: OpenApiService, sessionRepository: SessionRepository, executionContext: ExecutionContext) extends PositionDataLoader {
  implicit val executionContextImplicit = executionContext
  implicit val openApiServiceImplicit = openApiService
  implicit val sessionRepositoryImplicit = sessionRepository

  override def loadClients(): Future[Either[String, Seq[Client]]] = OpenApiCallingContext.call(
    (api, cont) => api.clients(cont),
    sessionId
  ).map {
    case Left(err) => Left(err.errorString)
    case Right(clients) => Right(clients.map(_.toModel))
  }

  override def loadPositions(clientKey: String): Future[Either[String, Seq[Position]]] = OpenApiCallingContext.call(
    (api, cont) => api.positions(None,None, clientKey, cont),
    sessionId
  ).map {
    case Left(err) => Left(err.errorString)
    case Right(clients) => Right(samplePositionsFor(clientKey)) //Right(clients.map(_.toModel))
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
  def apply(sessionId : String)(implicit openApiService: OpenApiService, sessionRepository: SessionRepository, executionContext: ExecutionContext) : PositionDataLoader =
    new RestPositionDataLoader(sessionId, openApiService, sessionRepository, executionContext)
}