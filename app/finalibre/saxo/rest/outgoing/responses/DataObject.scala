package finalibre.saxo.rest.outgoing.responses

case class DataObject[A](
                        Data : Seq[A]
                        )
