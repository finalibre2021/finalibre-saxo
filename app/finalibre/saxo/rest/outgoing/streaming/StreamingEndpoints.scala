package finalibre.saxo.rest.outgoing.streaming

import finalibre.saxo.configuration.SaxoConfig
import finalibre.saxo.rest.outgoing.streaming.topics._

import java.util.UUID

object StreamingEndpoints {

  object AutoTrading {
    val Group = "at"
    object Investments {
      val SubGroup = "investments"
      val Version = 1
      val Investments = StreamingEndpoint[InvestmentTopic]("AutoTradingInvestments", Group, Version, SubGroup)
      val Suggestions = StreamingEndpoint[InvestmentSuggestionTopic]("AutoTradingInvestmentSuggestions", Group, Version, s"$SubGroup/suggestions")
    }
  }

  object Chart {
    val Group = "chart"
    object Charts {
      val SubGroup = "charts"
      val Version = 1
      val Charts = StreamingEndpoint[ChartTopic]("Charts", Group, Version, SubGroup)
    }
  }

  object Portfolio {
    val Group = "port"
    object Accounts {
      val SubGroup = "accounts"
      val Version = 1
      val Accounts = StreamingEndpoint[AccountTopic]("PortfolioAccounts", Group, Version, SubGroup)
    }
    object Balances {
      val SubGroup = "balances"
      val Version = 1
      val Balances = StreamingEndpoint[BalanceTopic]("PortfolioBalances", Group, Version, SubGroup)
    }
    object ClosedPositions {
      val SubGroup = "closedpositions"
      val Version = 1
      val ClosedPositions = StreamingEndpoint[ClosedPositionTopic]("PortfolioClosedPositions", Group, Version, SubGroup)
    }
    object Exposure {
      val SubGroup = "exposure"
      val Version = 1
      val Instruments = StreamingEndpoint[ExposureTopic]("PortfolioExposureInstruments", s"$Group/instruments", Version, SubGroup)
    }
    object NetPositions {
      val SubGroup = "netpositions"
      val Version = 1
      val NetPositions = StreamingEndpoint[NetPositionTopic]("PortfolioNetPositions", Group, Version, SubGroup)
    }
    object Orders {
      val SubGroup = "orders"
      val Version = 1
      val Orders = StreamingEndpoint[OrderTopic]("PortfolioOrders", Group, Version, SubGroup)
    }
    object Positions {
      val SubGroup = "positions"
      val Version = 1
      val Positions = StreamingEndpoint("PortfolioPositions", Group, Version, SubGroup)
    }
  }

  object RootServices {
    val Group = "root"
    object Availability {
      val SubGroup = "availability"
      val Version = 1
      val Availability = StreamingEndpoint("RootAvailability", Group, Version, SubGroup)
    }

    object Sessions {
      val SubGroup = "sessions"
      val Version = 1
      val Events = StreamingEndpoint("RootSessionEvents", Group, Version, s"$SubGroup/events")
    }
  }

  object Trading {
    val Group = "trade"
    object InfoPrices {
      val SubGroup = "infoprices"
      val Version = 1
      val InfoPrices = StreamingEndpoint("TradeInfoPrices", Group, Version, SubGroup)
    }
    object Messages {
      val SubGroup = "messages"
      val Version = 1
      val Messages = StreamingEndpoint("TradeMessages", Group, Version, SubGroup)
    }
    object OptionsChain {
      val SubGroup = "optionschain"
      val Version = 1
      val OptionsChain = StreamingEndpoint("TradeOptionsChain", Group, Version, SubGroup)
    }
    object Prices {
      val SubGroup = "prices"
      val Version = 1
      val Prices = StreamingEndpoint("TradePrices", Group, Version, SubGroup)
      val MultilegPrices = StreamingEndpoint("TradeMultiLegPrices", Group, Version, s"$SubGroup/multileg")
    }

  }




  case class StreamingEndpoint[T <: StreamingTopic](
                              name : String,
                              group : String,
                              version : Int,
                              subGroup : String,

                              manualUrl : Option[String] = None
                              ) {
    lazy val endpointId = nextId
    lazy val referenceId = referenceIdFrom(endpointId, name)

    def subscriptionUrl(contextId : String) : String =
      manualUrl.getOrElse(s"${SaxoConfig.Rest.Outgoing.openApiBaseUrl}/$group/v$version/$subGroup/subscriptions/$contextId/$referenceId")
  }

  private def referenceIdFrom(id : Long, name : String) =
    (id + name).replaceAll("[^([A-Z]|[a-z]|[0-9])]", "").substring(0,10) +
      UUID.fromString(name).toString.replaceAll("-","")


  private var currentId = 0L
  def nextId : Long = {
    currentId += 1
    currentId
  }



}
