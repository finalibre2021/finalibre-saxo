package finalibre.saxo.positions.mappers

import finalibre.saxo.client.positions.model.ClientDto
import finalibre.saxo.positions.model.Client
import finalibre.saxo.rest.outgoing.responses.ResponseClient

object ClientMappers {

  implicit class ResponseClientMapper(resp : ResponseClient) {
    def toModel = Client(
      resp.clientId,
      resp.clientKey,
      resp.name,
      resp.defaultAccountId,
      resp.defaultCurrency
    )
  }

  implicit class ClientMapper(client : Client){
    def toDto = ClientDto (
      client.clientId,
      client.clientKey,
      client.name
    )
  }

}
