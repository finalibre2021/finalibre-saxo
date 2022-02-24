package finalibre.saxo.rest.outgoing.streaming.topics

case class TransactionsNotBookedDetails(
                                         accrual : Double,
                                         additionalTransactionCost : Double,
                                         bondValue : Double,
                                         cashDeposit : Double,
                                         cashReservation : Double,
                                         cashWithdrawal : Double,
                                         certificatesValue : Double,
                                         commission : Double,
                                         exchangeFee : Double,
                                         externalCharges : Double,
                                         fundsReservedByOrder : Double,
                                         ipoSubscriptionFee : Double,
                                         leveragedKnockOutProductsValue : Double,
                                         mutualFundValue : Double,
                                         optionPremium : Double,
                                         shareValue : Double,
                                         stampDuty : Double,
                                         warrantPremium : Double
                                       )

