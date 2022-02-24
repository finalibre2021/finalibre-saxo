package finalibre.saxo.rest.outgoing.streaming.topics

case class MarginOverviewContributor(
                                      assetTypes : Seq[String],
                                      instrumentDescription : String,
                                      instrumentSpecifier : String,
                                      margin : String,
                                      uic : Long
                                    )
