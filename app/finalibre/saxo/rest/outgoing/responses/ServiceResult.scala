package finalibre.saxo.rest.outgoing.responses

object ServiceResult {
  type CallResult[A] = Either[ServiceError, A]
  sealed abstract class ServiceError {
    def errorString : String
  }
  case object AuthorizationError extends ServiceError {
    override def errorString = "Not authorized"
  }
  case class OtherHttpStatusError(httpCode : Int, codeDescription : String) extends ServiceError {
    override def errorString = s"$httpCode - $codeDescription"

  }
  case class ExceptionError(exc : Throwable) extends ServiceError {
    override def errorString = exc.getMessage
  }
  case class OtherError(str : String) extends ServiceError {
    override def errorString = str
  }


}



