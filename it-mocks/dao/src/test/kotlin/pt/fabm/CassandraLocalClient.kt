package pt.fabm

import io.reactivex.Single
import io.vertx.reactivex.cassandra.CassandraClient
import io.vertx.reactivex.cassandra.ResultSet
import io.vertx.reactivex.core.Vertx

class CassandraLocalClient(vertx: Vertx) {
  val cassandraClient: Single<CassandraClient> = Single.fromCallable {
    println("initializated client")
    CassandraClient.createNonShared(vertx)
  }.cache()

  fun executeString(command: String): Single<ResultSet> =
    cassandraClient.flatMap {it.rxExecute(command)}

}
