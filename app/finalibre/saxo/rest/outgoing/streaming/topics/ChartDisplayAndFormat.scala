package finalibre.saxo.rest.outgoing.streaming.topics

case class ChartDisplayAndFormat(
                             currency : String,
                             decimals : Int,
                             description : String,
                             format : String,
                             symbol : String
                           )
