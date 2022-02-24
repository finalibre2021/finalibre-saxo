package finalibre.saxo.rest.outgoing.streaming.requests

import finalibre.saxo.rest.outgoing.streaming.requests.SubscriptionRequest.capFirst

trait SubscriptionRequest extends Product {
  def asMap = (for(i <- 0 until this.productArity) yield productElementName(i) -> productElement(i))
    .filter(p => p._2 != None)
    .toMap
    .collect {
      case (nam,Some(v)) => capFirst(nam) -> v
      case (nam, v) => capFirst(nam) -> v
    }

}

object SubscriptionRequest {
  def capFirst(str : String) : String = if(str.isBlank) str else str(0).toUpper + str.drop(1)
}
