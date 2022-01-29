import finalibre.saxo.security.db.postgres.PostgresSessionRepository

import scala.concurrent.ExecutionContext

object TableCreation {
  def main(args: Array[String]): Unit = {
    val repo = new PostgresSessionRepository(ExecutionContext.global)
    repo.createTables()
  }
}
