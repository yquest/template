package pt.fabm

import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient
import pt.fabm.template.dao.DaoVerticle

class ClientMock(private val vertx: Vertx) {
  private val client = WebClient.create(vertx)
  private val port = 8081
  private val host = "localhost"

  fun listUsers(): Single<JsonArray> {
    return client.post(port, host, "/event-bus")
      .rxSendJsonObject(JsonObject().put(ADDRESS,DaoVerticle.USER_LIST))
      .map { it.bodyAsJsonArray() }
  }

  companion object {
    private const val ADDRESS: String = "address"
  }
}
