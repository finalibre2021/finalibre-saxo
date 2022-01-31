package finalibre.saxo.rest.outgoing.responses

object ServiceResult {
  type CallResult[A] = Either[ServiceError, A]
  sealed abstract class ServiceError
  case object AuthorizationError extends ServiceError
  case class OtherHttpStatusError(httpCode : Int, codeDescription : String) extends ServiceError
  case class ExceptionError(exc : Throwable) extends ServiceError
  case class OtherError(str : String) extends ServiceError


}



