package finalibre.saxo.rest.outgoing.streaming.topics

case class MarginOverviewGroup(
                                contributors : Seq[MarginOverviewContributor],
                                groupType : String,
                                totalMargin : Double
                              )
