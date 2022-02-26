package finalibre.saxo.rest.outgoing

object Enums {
  object Horizon extends Enumeration {
    type Horizon = Val
    protected case class HorizonValue(inMinutes : Int) extends Val
    val Minute1 =  HorizonValue(1)
    val Minute5 =  HorizonValue(5)
    val Minute10 = HorizonValue(10)
    val Minute15 = HorizonValue(15)
    val Minute30 = HorizonValue(30)
    val Hour1 = HorizonValue(60)
    val Hour2 = HorizonValue(120)
    val Hour4 = HorizonValue(240)
    val Hour6 = HorizonValue(360)
    val Hour8 = HorizonValue(480)
    val Day1 = HorizonValue(1440)
    val Day7 = HorizonValue(10080)
    val Day30 = HorizonValue(43200)
    val allValues = List(Minute1, Minute5, Minute10, Minute15, Minute30,
      Hour1, Hour2, Hour4, Hour6, Hour8, Day1, Day7, Day30)
    def fromMinutes(minutes : Int) = allValues.find(_.inMinutes == minutes).getOrElse(Minute1)

  }

  object AssetType extends Enumeration {
    type AssetType = Val
    protected case class AssetTypeValue(asString : String) extends Val
    val Bond = AssetTypeValue("Bond")
    val Cash = AssetTypeValue("Cash")
    val CertificateBonus = AssetTypeValue("CertificateBonus")
    val CertificateCappedBonus = AssetTypeValue("CertificateCappedBonus")
    val CertificateCappedCapitalProtected = AssetTypeValue("CertificateCappedCapitalProtected")
    val CertificateCappedOutperformance = AssetTypeValue("CertificateCappedOutperformance")
    val CertificateConstantLeverage = AssetTypeValue("CertificateConstantLeverage")
    val CertificateDiscount = AssetTypeValue("CertificateDiscount")
    val CertificateExpress = AssetTypeValue("CertificateExpress")
    val CertificateTracker = AssetTypeValue("CertificateTracker")
    val CertificateUncappedCapitalProtection = AssetTypeValue("CertificateUncappedCapitalProtection")
    val CertificateUncappedOutperformance = AssetTypeValue("CertificateUncappedOutperformance")
    val CfdIndexOption = AssetTypeValue("CfdIndexOption")
    val CfdOnEtc = AssetTypeValue("CfdOnEtc")
    val CfdOnEtf = AssetTypeValue("CfdOnEtf")
    val CfdOnEtn = AssetTypeValue("CfdOnEtn")
    val CfdOnFund = AssetTypeValue("CfdOnFund")
    val CfdOnFutures = AssetTypeValue("CfdOnFutures")
    val CfdOnIndex = AssetTypeValue("CfdOnIndex")
    val CfdOnRights = AssetTypeValue("CfdOnRights")
    val CfdOnStock = AssetTypeValue("CfdOnStock")
    val CompanyWarrant = AssetTypeValue("CompanyWarrant")
    val ContractFutures = AssetTypeValue("ContractFutures")
    val Etc = AssetTypeValue("Etc")
    val Etf = AssetTypeValue("Etf")
    val Etn = AssetTypeValue("Etn")
    val Fund = AssetTypeValue("Fund")
    val FuturesOption = AssetTypeValue("FuturesOption")
    val FuturesStrategy = AssetTypeValue("FuturesStrategy")
    val FxBinaryOption = AssetTypeValue("FxBinaryOption")
    val FxForwards = AssetTypeValue("FxForwards")
    val FxKnockInOption = AssetTypeValue("FxKnockInOption")
    val FxKnockOutOption = AssetTypeValue("FxKnockOutOption")
    val FxNoTouchOption = AssetTypeValue("FxNoTouchOption")
    val FxOneTouchOption = AssetTypeValue("FxOneTouchOption")
    val FxSpot = AssetTypeValue("FxSpot")
    val FxVanillaOption = AssetTypeValue("FxVanillaOption")
    val GuaranteeNote = AssetTypeValue("GuaranteeNote")
    val IpoOnStock = AssetTypeValue("IpoOnStock")
    val MiniFuture = AssetTypeValue("MiniFuture")
    val MutualFund = AssetTypeValue("MutualFund")
    val PortfolioNote = AssetTypeValue("PortfolioNote")
    val Rights = AssetTypeValue("Rights")
    val SrdOnEtf = AssetTypeValue("SrdOnEtf")
    val SrdOnStock = AssetTypeValue("SrdOnStock")
    val Stock = AssetTypeValue("Stock")
    val StockIndex = AssetTypeValue("StockIndex")
    val StockIndexOption = AssetTypeValue("StockIndexOption")
    val StockOption = AssetTypeValue("StockOption")
    val Warrant = AssetTypeValue("Warrant")
    val WarrantDoubleKnockOut = AssetTypeValue("WarrantDoubleKnockOut")
    val WarrantKnockOut = AssetTypeValue("WarrantKnockOut")
    val WarrantOpenEndKnockOut = AssetTypeValue("WarrantOpenEndKnockOut")
    val WarrantSpread = AssetTypeValue("WarrantSpread")

    val allValues = List(Bond, Cash, CertificateBonus, CertificateCappedBonus, CertificateCappedOutperformance,
      CertificateConstantLeverage, CertificateDiscount, CertificateExpress, CertificateTracker, CertificateUncappedCapitalProtection, CertificateUncappedOutperformance,
      CfdIndexOption, CfdOnEtc, CfdOnEtf, CfdOnEtn, CfdOnFund, CfdOnFutures, CfdOnIndex, CfdOnRights,
      CfdOnStock, CompanyWarrant, ContractFutures, Etc, Etf, Etn, Fund, FuturesOption, FuturesStrategy, FxBinaryOption,
      FxForwards, FxKnockInOption, FxKnockOutOption, FxNoTouchOption, FxOneTouchOption, FxSpot,
      FxVanillaOption, GuaranteeNote, IpoOnStock, MiniFuture, MutualFund, PortfolioNote, Rights, SrdOnEtf, SrdOnStock,
      Stock, StockIndex, StockIndexOption, StockOption, Warrant, WarrantDoubleKnockOut, WarrantKnockOut, WarrantOpenEndKnockOut,
      WarrantSpread)

    def fromString(str : String) = allValues.find(_.asString == str)

  }

  object ChartFieldSpec extends Enumeration {
    type ChartFieldSpec = Val
    protected case class ChartFieldValue(idStr : String, description : String) extends Val
    val BlockInfo =  ChartFieldValue("BlockInfo","Return information about previous blocks")
    val ChartInfo =  ChartFieldValue("ChartInfo","Return information about the time series")
    val Data =  ChartFieldValue("BlockInfo","Return the chart data samples")
    val DisplayAndFormat =  ChartFieldValue("DisplayAndFormat","Return information about the instrument")

    val allValues = List(BlockInfo, ChartInfo, Data, DisplayAndFormat)
    def fromString(str : String) = allValues.find(_.idStr == str)
  }

  /*
   */

}
