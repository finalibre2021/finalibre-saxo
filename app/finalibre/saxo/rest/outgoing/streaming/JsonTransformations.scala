package finalibre.saxo.rest.outgoing.streaming
import finalibre.saxo.rest.outgoing.streaming.topics._
import play.api.libs.json.{Json => PlayJson}
import play.api.libs.json.{Reads => PlayReads}

object JsonTransformations {




  object Reads {
    // No dependencies
    implicit val accountEntryReads : PlayReads[AccountEntry] = PlayJson.reads[AccountEntry]
    implicit val lineStatusReads : PlayReads[LineStatus] = PlayJson.reads[LineStatus]
    implicit val initialMarginReads : PlayReads[InitialMargin] = PlayJson.reads[InitialMargin]
    implicit val instrumentCollateralDetailReads : PlayReads[InstrumentCollateralDetail] = PlayJson.reads[InstrumentCollateralDetail]
    implicit val marginOverviewContributorReads : PlayReads[MarginOverviewContributor] = PlayJson.reads[MarginOverviewContributor]
    implicit val transactionsNotBookedReads : PlayReads[TransactionsNotBookedDetails] = PlayJson.reads[TransactionsNotBookedDetails]
    implicit val chartDisplayAndFormatReads : PlayReads[ChartDisplayAndFormat] = PlayJson.reads[ChartDisplayAndFormat]
    implicit val chartDataEntryReads : PlayReads[ChartDataEntry] = PlayJson.reads[ChartDataEntry]
    implicit val chartInfoReads : PlayReads[ChartInfo] = PlayJson.reads[ChartInfo]
    implicit val fxOptionsBaseDataReads : PlayReads[FxOptionsBaseData] = PlayJson.reads[FxOptionsBaseData]
    implicit val orderDurationReads : PlayReads[OrderDuration] = PlayJson.reads[OrderDuration]
    implicit val fixedIncomeReads : PlayReads[FixedIncomeData] = PlayJson.reads[FixedIncomeData]
    implicit val subPositionViewReads : PlayReads[SubPositionView] = PlayJson.reads[SubPositionView]
    implicit val settlementInstructionsReads : PlayReads[SettlementInstructions] = PlayJson.reads[SettlementInstructions]
    implicit val expiryDataReads : PlayReads[ExpiryData] = PlayJson.reads[ExpiryData]
    implicit val optionsDataReads : PlayReads[OptionsData] = PlayJson.reads[OptionsData]
    implicit val multiLegOrderDetailsReads : PlayReads[MultiLegOrderDetails] = PlayJson.reads[MultiLegOrderDetails]
    implicit val greeksReads : PlayReads[GreeksDetails] = PlayJson.reads[GreeksDetails]
    implicit val instrumentDisplayReads : PlayReads[InstrumentDisplayAndFormat] = PlayJson.reads[InstrumentDisplayAndFormat]
    implicit val instrumentExchangeReads : PlayReads[InstrumentExchangeDetails] = PlayJson.reads[InstrumentExchangeDetails]
    implicit val commissionsReads : PlayReads[Commissions] = PlayJson.reads[Commissions]
    implicit val historicalChangesReads : PlayReads[HistoricalChanges] = PlayJson.reads[HistoricalChanges]
    implicit val instrumentPriceReads : PlayReads[InstrumentPriceDetails] = PlayJson.reads[InstrumentPriceDetails]
    implicit val marginImpactReads : PlayReads[MarginImpactBuySell] = PlayJson.reads[MarginImpactBuySell]
    implicit val marketDepthReads : PlayReads[MarketDepth] = PlayJson.reads[MarketDepth]
    implicit val priceInfoReads : PlayReads[PriceInfo] = PlayJson.reads[PriceInfo]
    implicit val priceInfoDetailsReads : PlayReads[PriceInfoDetails] = PlayJson.reads[PriceInfoDetails]
    implicit val priceQuoteReads : PlayReads[PriceQuote] = PlayJson.reads[PriceQuote]
    implicit val priceTimestampsReads : PlayReads[PriceTimestamps] = PlayJson.reads[PriceTimestamps]
    implicit val tradableReads : PlayReads[TradableQuote] = PlayJson.reads[TradableQuote]
    implicit val quoteReads : PlayReads[Quote] = PlayJson.reads[Quote]

    // 1 dependency
    implicit val multiLegOrderPriceReads : PlayReads[MultiLegOrderPriceLeg] = PlayJson.reads[MultiLegOrderPriceLeg]
    implicit val marginCollateralNotAvailableReads : PlayReads[MarginCollateralNotAvailableDetail] = PlayJson.reads[MarginCollateralNotAvailableDetail]
    implicit val marginOverviewGroupReads : PlayReads[MarginOverviewGroup] = PlayJson.reads[MarginOverviewGroup]
    implicit val relatedOrderReads : PlayReads[RelatedOrderInfo] = PlayJson.reads[RelatedOrderInfo]
    implicit val subPositionBaseReads : PlayReads[SubPositionBase] = PlayJson.reads[SubPositionBase]

    // 2 or more dependencies
    implicit val subPositionResponseReads : PlayReads[SubPositionResponse] = PlayJson.reads[SubPositionResponse]
    implicit val netPositionDynamicReads : PlayReads[NetPositionDynamic] = PlayJson.reads[NetPositionDynamic]
    implicit val positionDynamicReads : PlayReads[PositionDynamic] = PlayJson.reads[PositionDynamic]
    implicit val netPositionStaticReads : PlayReads[NetPositionStatic] = PlayJson.reads[NetPositionStatic]
    implicit val positionStaticReads : PlayReads[PositionStatic] = PlayJson.reads[PositionStatic]

    // Topics
    implicit val accountReads : PlayReads[AccountTopic] = PlayJson.reads[AccountTopic]
    implicit val balanceReads : PlayReads[BalanceTopic] = PlayJson.reads[BalanceTopic]
    implicit val chartReads : PlayReads[ChartTopic] = PlayJson.reads[ChartTopic]
    implicit val closedPositionReads : PlayReads[ClosedPositionTopic] = PlayJson.reads[ClosedPositionTopic]
    implicit val exposureReads : PlayReads[ExposureTopic] = PlayJson.reads[ExposureTopic]
    implicit val featureAvailabilityReads : PlayReads[FeatureAvailabilityTopic] = PlayJson.reads[FeatureAvailabilityTopic]
    implicit val investmentSuggestionReads : PlayReads[InvestmentSuggestionTopic] = PlayJson.reads[InvestmentSuggestionTopic]
    implicit val investmentReads : PlayReads[InvestmentTopic] = PlayJson.reads[InvestmentTopic]
    implicit val netPositionReads : PlayReads[NetPositionTopic] = PlayJson.reads[NetPositionTopic]
    implicit val optionsChainReads : PlayReads[OptionsChainTopic] = PlayJson.reads[OptionsChainTopic]
    implicit val orderReads : PlayReads[OrderTopic] = PlayJson.reads[OrderTopic]
    implicit val positionReads : PlayReads[PositionTopic] = PlayJson.reads[PositionTopic]
    implicit val priceTopicReads : PlayReads[PriceTopic] = PlayJson.reads[PriceTopic]
    implicit val sessionStateReads : PlayReads[SessionStateTopic] = PlayJson.reads[SessionStateTopic]
    implicit val tradeMessageReads : PlayReads[TradeMessageTopic] = PlayJson.reads[TradeMessageTopic]



  }

}
