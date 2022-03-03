package finalibre.saxo.rest.outgoing.responses

import finalibre.saxo.rest.outgoing.streaming.StreamingSubscription
import finalibre.saxo.rest.outgoing.streaming.topics.StreamingTopic

import scala.reflect.ClassTag

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
  case class JsonConversionError[A](jsonString : String)(implicit ct : ClassTag[A]) extends ServiceError {
    import scala.reflect._
    override def errorString: String = s"Failed to convert following json string to: ${ct.runtimeClass} - json string: ${jsonString}"
  }

  type SubscriptionResult[A <: StreamingTopic] = CallResult[StreamingSubscription[A]]


}



