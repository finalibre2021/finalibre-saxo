package finalibre.saxo.rest.outgoing.streaming.requests

trait SubscriptionRequest extends Product {
  def asMap = (for(i <- 0 until this.productArity) yield productElementName(i) -> productElement(i))
    .filter(p => p._2 != None)
    .toMap
    .collect {
      case (nam,Some(v)) => nam -> v
      case oth => oth
    }

}
