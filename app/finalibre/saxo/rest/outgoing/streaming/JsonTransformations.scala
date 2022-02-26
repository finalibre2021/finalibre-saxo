package finalibre.saxo.rest.outgoing.streaming
import finalibre.saxo.rest.outgoing.Encoding
import finalibre.saxo.rest.outgoing.streaming.topics._
import play.api.libs.json.{Json => PlayJson, Reads => PlayReads, JsSuccess => PlayJsSuccess, JsError => PlayJsError, JsValue => PlayJsValue}
import io.circe.{Json => CirceJson}

import scala.reflect.{ClassManifest, ClassTag}
import scala.util.{Failure, Success, Try}

object JsonTransformations {
  private implicit val playJsonParseConfig = Encoding.DefaultConfiguration

  def parseTopic[T <: StreamingTopic](json : CirceJson) : Either[String, T] = {
    implicit val reads = Reads.playReadsFor[T]
    convert[T](json)
  }

  def parseTopicSequence[T <: StreamingTopic](json : CirceJson) :  Either[String, Seq[T]] = {
    implicit val simpleReads = Reads.playReadsFor[T]
    implicit val seqReads : PlayReads[Seq[T]] = PlayJson.reads[Seq[T]]
    convert[Seq[T]](json)
  }

  private def convert[A](json : CirceJson)(implicit reads : PlayReads[A]) : Either[String, A] = {
    val asString = json.noSpaces
    Try{PlayJson.parse(asString)} match {
      case Failure(err) => Left(s"Failed to parse JSON: ${json.spaces2} with error: ${err.getMessage}")
      case Success(asPlayJson) => asPlayJson.validate[A] match {
        case PlayJsSuccess(converted,_) => Right(converted)
        case PlayJsError(errs) => Left(s"Failed to convert JSON: ${json.spaces2} to class: ${classOf[A].toString} with errors: " +
          s"${errs.flatMap(_._2.map(_.message)).mkString(", ")}")
      }

    }
  }





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

    def playReadsFor[T <: StreamingTopic](implicit classTag : ClassTag[T]) : PlayReads[T] = classTag.runtimeClass match {
      case AccountTopic_ => accountReads.asInstanceOf[PlayReads[T]]
      case BalanceTopic_ => balanceReads.asInstanceOf[PlayReads[T]]
      case ChartTopic_ => chartReads.asInstanceOf[PlayReads[T]]
      case ClosedPositionTopic_ => closedPositionReads.asInstanceOf[PlayReads[T]]
      case ExposureTopic_ => exposureReads.asInstanceOf[PlayReads[T]]
      case FeatureAvailabilityTopic_ => featureAvailabilityReads.asInstanceOf[PlayReads[T]]
      case InvestmentSuggestionTopic_ => investmentSuggestionReads.asInstanceOf[PlayReads[T]]
      case InvestmentTopic_ => investmentReads.asInstanceOf[PlayReads[T]]
      case NetPositionTopic_ => netPositionReads.asInstanceOf[PlayReads[T]]
      case OptionsChainTopic_ => optionsChainReads.asInstanceOf[PlayReads[T]]
      case OrderTopic_ => orderReads.asInstanceOf[PlayReads[T]]
      case PositionTopic_ => positionReads.asInstanceOf[PlayReads[T]]
      case PriceTopic_ => priceTopicReads.asInstanceOf[PlayReads[T]]
      case SessionStateTopic_ => sessionStateReads.asInstanceOf[PlayReads[T]]
      case TradeMessageTopic_ => tradeMessageReads.asInstanceOf[PlayReads[T]]
      case _ => accountReads.asInstanceOf[PlayReads[T]]
    }



    private val AccountTopic_ = classOf[AccountTopic]
    private val BalanceTopic_ = classOf[BalanceTopic]
    private val ChartTopic_ = classOf[ChartTopic]
    private val ClosedPositionTopic_ = classOf[ClosedPositionTopic]
    private val ExposureTopic_ = classOf[ExposureTopic]
    private val FeatureAvailabilityTopic_ = classOf[FeatureAvailabilityTopic]
    private val InvestmentSuggestionTopic_ = classOf[InvestmentSuggestionTopic]
    private val InvestmentTopic_ = classOf[InvestmentTopic]
    private val NetPositionTopic_ = classOf[NetPositionTopic]
    private val OptionsChainTopic_ = classOf[OptionsChainTopic]
    private val OrderTopic_ = classOf[OrderTopic]
    private val PositionTopic_ = classOf[PositionTopic]
    private val PriceTopic_ = classOf[PriceTopic]
    private val SessionStateTopic_ = classOf[SessionStateTopic]
    private val TradeMessageTopic_ = classOf[TradeMessageTopic]





  }

}
