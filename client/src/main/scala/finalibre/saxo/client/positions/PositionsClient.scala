package finalibre.saxo.client.positions

import finalibre.saxo.client.positions.messages.{ToClientMessage, ToServerMessage}
import finalibre.saxo.client.positions.model.{ClientDto, PositionDto}
import finalibre.saxo.client.util.WSConnector
import finalibre.saxo.client.util.tables.Column._
import finalibre.saxo.client.util.tables.TableBuilder
import io.circe.Json
import org.querki.jquery.{$, JQuery}
import org.scalajs.dom.Event

import scala.collection.mutable
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("PositionsClient")
object PositionsClient {
  private val ClientsInsertDivId = "#clients-insert-id"
  private val PositionsInsertDivId = "#positions-insert-id"

  private var clients : Seq[ClientDto] = List.empty[ClientDto]
  private val checkedClients : mutable.HashSet[String] = mutable.HashSet.empty[String]
  private val clientsInputs : mutable.ArrayBuffer[JQuery] = mutable.ArrayBuffer.empty[JQuery]
  private var positions : Seq[PositionDto] = List.empty[PositionDto]


  object PositionsTables {
    val idCol = StringColumn[PositionDto]("id", "Position ID", pos => Some(pos.positionId))
    val accountIdCol = StringColumn[PositionDto]("accountid", "Account ID", pos => Some(pos.accountId))
    val assetTypeCol = StringColumn[PositionDto]("assettype", "Asset type", pos => Some(pos.assetType))
    val exposureCurrencyCol = StringColumn[PositionDto]("exposurecurrency", "Exposure currency", pos => Some(pos.exposureCurrency))
    val statusCol = StringColumn[PositionDto]("status", "Status", pos => Some(pos.status))
    val amountCol = DoubleColumn[PositionDto]("amount", "Amount", pos => Some(pos.amount))
    val currentPriceCol = DoubleColumn[PositionDto]("currentprice", "Current price", pos => Some(pos.currentPrice))
    val currentPriceDelayCol = LongColumn[PositionDto]("currentpricedelay", "Current price delay (minutes)", pos => Some(pos.currentPriceDelayMinutes))
    val currentPriceTypeCol = StringColumn[PositionDto]("currentpricetype", "Current price type", pos => Some(pos.currentPriceType))
    val exposureCol = DoubleColumn[PositionDto]("exposure", "Exposure", pos => Some(pos.exposure))
    val exposureInBaseCol = DoubleColumn[PositionDto]("exposureinbase", "Exposure (base ccy)", pos => Some(pos.exposureInBaseCurrency))
    val profitLossCol = DoubleColumn[PositionDto]("profitloss", "Profit/loss", pos => Some(pos.profitLossOnTrade))
    val profitLossInBaseCol = DoubleColumn[PositionDto]("profitlossinBase", "Profit/loss (base ccy)", pos => Some(pos.profitLossOnTradeInBaseCurrency))
    val tradeCostCol = DoubleColumn[PositionDto]("tradecost", "Trade costs", pos => Some(pos.tradeCostsTotal))
    val tradeCostInBaseCol = DoubleColumn[PositionDto]("tradecostinbase", "Trade costs (base ccy)", pos => Some(pos.tradeCostsTotalInBaseCurrency))

    val columns = Seq(idCol, accountIdCol, assetTypeCol, exposureCurrencyCol, statusCol, amountCol,
      currentPriceCol,currentPriceDelayCol, currentPriceTypeCol, exposureCol, exposureInBaseCol, profitLossCol,
      profitLossInBaseCol, tradeCostCol, tradeCostInBaseCol)

    val tableBuilder = TableBuilder[PositionDto]("positions-table", columns)


  }




  @JSExport
  def main() : Unit = {
    PositionsConnector.connectToServer
  }

  private def fillClients(): Unit = {
    $(ClientsInsertDivId).empty()
    clientsInputs.clear()
    val clientsElements = clients.map {
      case cli => {
        val el = $(s"<input class='form-check-input me-1' type='checkbox' aria-label='${cli.name}'>")
        clientsInputs.append(el)
        if(checkedClients.contains(cli.clientKey))
          $(el).attr("checked", "checked")
        $(el).change(() => {
          if($(el).attr("checked") != null)
            checkedClients.add(cli.clientKey)
          else checkedClients.remove(cli.clientKey)
          setClientsEnabled(false)
          PositionsConnector.requestPositions()
        })
        $("<li class='list-group-item'>").append(
          el,
          cli.name
        )
      }
    }
    val grp = $("<ul class='list-group'>")
    clientsElements.foreach(el => $(grp).append(el))
    $(ClientsInsertDivId).append(grp)
  }

  private def fillPositions() : Unit = {
    $(PositionsInsertDivId).empty()
    val clientName = clients
      .map(cl => cl.clientId -> cl.name)
      .toMap
    positions
      .groupBy(pos => pos.clientId)
      .map(grp => if(clientName.contains(grp._1)) (0, clientName(grp._1), grp._1, grp._2) else (1,s"Client: ${grp._1}", grp._1, grp._2))
      .toList
      .sortBy(tri => (tri._1, tri._2))
      .foreach {
        case (_,clientName, clientId, poss) => {
          val row = $("<div class='row mt-2'>")
          $(row).append(
            $("<div class='shadow p-3 mb-5 bg-body rounded'>").text(clientName)
          )
          val sortedPos = poss.sortBy(pos => (pos.assetType, pos.exposureCurrency, - pos.exposure))
          val table = PositionsTables.tableBuilder.buildTable(sortedPos, Some(s"positions-table-${clientId.replaceAll(" ", "")}"))
          $(row).append(
            table
          )
          $(PositionsInsertDivId).append(row)
        }
      }
  }

  private def setClientsEnabled(enabled : Boolean) : Unit = {
    if(enabled) {
      clientsInputs.foreach(cli => $(cli).removeAttr("disabled"))
    }
    else {
      clientsInputs
        .filter(cli => $(cli).attr("disabled") == null)
        .foreach(cli => $(cli).attr("disabled", "disabled"))
    }

  }




  object PositionsConnector extends WSConnector {
    import io.circe.generic.auto._
    import io.circe.syntax._

    override def onOpen: Event => Unit = (ev) =>  requestInitialData()

    override def onJson: Option[Json => Unit] = Some(
      (js : Json) => js.as[ToClientMessage].foreach {
        case mess => {
          println(s"Received message from backend: $mess")
          mess.clients.foreach {
            case clies => {
              clients = clies
              fillClients()
            }
          }
          mess.positions.foreach {
            case poss => {
              positions = poss
              setClientsEnabled(true)
              fillPositions()
            }
          }
        }
      }
    )

    def requestInitialData() : Unit = {
      val mess = ToServerMessage(ToServerMessage.RequestInitialDataMessageType)
      send(mess)
    }

    def requestPositions() : Unit =  {
      val mess = ToServerMessage(
        ToServerMessage.SelectClientsMessageType,
        clientKeys = Some(checkedClients.toList)
      )
      send(mess)
    }


    def send(mess : ToServerMessage) : Unit = {
      val asJson : Json = mess.asJson
      super.!(asJson.toString)
    }

  }

}
