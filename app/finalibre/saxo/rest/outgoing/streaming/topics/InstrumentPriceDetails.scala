package finalibre.saxo.rest.outgoing.streaming.topics

import java.time.LocalDateTime

case class InstrumentPriceDetails(
                                   accruedInterest : Option[Double],
                                   askYield : Option[Double],
                                   bidYield : Option[Double],
                                   cfdBorrowingCost : Option[Double],
                                   cdHardToFinanceRate : Option[Double],
                                   cfdPriceAdjustment : Option[Boolean],
                                   dma : Option[Boolean],
                                   estPriceBuy : Option[Double],
                                   estPriceSell : Option[Double],
                                   expiryDate : Option[LocalDateTime],
                                   forwardDateFarLeg : Option[LocalDateTime],
                                   forwardDateNearLeg : Option[LocalDateTime],
                                   indexRatio : Option[Double],
                                   isMarketOpen : Option[Boolean],
                                   lowerBarrier : Option[Double],
                                   midForwardPrice : Option[Double],
                                   midSpotPrice : Option[Double],
                                   midYield : Option[Double],
                                   noticeDate : Option[LocalDateTime],
                                   openInterest : Option[Double],
                                   paidCfdInterest : Option[Double],
                                   paidSrdInterest : Option[Double],
                                   receivedCfdInterest : Option[Double],
                                   receivedSrdInterest : Option[Double],
                                   shortTradeDisabled : Option[Boolean],
                                   spotAsk : Option[Double],
                                   spotBid : Option[Double],
                                   spotDate : Option[LocalDateTime],
                                   srdLastTradeDate : Option[LocalDateTime],
                                   srdSettlementDate : Option[LocalDateTime],
                                   strikePrice : Option[Double],
                                   swapAsk : Option[Double],
                                   swapBid : Option[Double],
                                   upperBarrier : Option[Double],
                                   valueDate : Option[LocalDateTime]
                                 )

