package finalibre.saxo.rest.outgoing.streaming.topics

case class SettlementInstructions(
                                   actualRolloverAmount : Double,
                                   actualSettlementAmount : Double,
                                   amount : Double,
                                   isSettlementInstructionsAllowed : Boolean,
                                   month : Int,
                                   settlementType : String,
                                   year : Int
                                 )
