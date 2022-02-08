package finalibre.saxo.positions

import finalibre.saxo.positions.model.{Account, Client, Position}

import scala.concurrent.Future

trait PositionDataLoader {
  def loadClients() : Future[Either[String, Seq[Client]]]
  def loadPositions(clientKey : String) : Future[Either[String, Seq[Position]]]

}
