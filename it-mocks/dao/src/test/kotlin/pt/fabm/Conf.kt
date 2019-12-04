package pt.fabm

import io.vertx.reactivex.core.Vertx

class Conf {
  var token: String? = null
  val vertx: Vertx = Vertx.vertx() ?: error("creating vertx error")
  val client: Client = Client(vertx)
  val clientMock = ClientMock(vertx)
  val cassandraLocalClient = CassandraLocalClient(vertx)
}
