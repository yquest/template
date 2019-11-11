package pt.fabm

import io.reactivex.Single
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.UserRegisterIn

class Client(vertx: Vertx) {
  private val client = WebClient.create(vertx)
  private val port = 8888
  private val host = "localhost"

  fun createUser(init: User.() -> Unit): Single<HttpResponse<Buffer>> {
    val user = User()
    user.init()
    return client.post(port, host, "/api/user")
      .rxSendJsonObject(
        JsonObject()
          .put(UserRegisterIn.NAME, user.name ?: error("no name"))
          .put(UserRegisterIn.PASS, (user.pass ?: error("no pass")).toByteArray())
          .put(UserRegisterIn.EMAIL, user.email ?: error("no email"))
      )
  }

  fun login(init: User.() -> Unit): Single<HttpResponse<Buffer>> {
    val user = User()
    user.init()
    return client.post(port, host, "/api/user/login")
      .rxSendJsonObject(
        JsonObject()
          .put("user", user.name ?: error("no name"))
          .put(
            UserRegisterIn.PASS, (user.pass ?: error("no pass"))
              .toByteArray()
          )
      )
  }
  fun createCar( cookie:String, init:CarEntry.()->Unit):Single<HttpResponse<Buffer>>{
    val carEntry = CarEntry()
    carEntry.init()
    return client.post(port, host, "/api/car")
      .putHeader(HttpHeaders.COOKIE.toString(),cookie)
      .rxSendJsonObject(
        JsonObject()
          .put(Car.MAKE, (carEntry.maker ?: error("no make")).ordinal)
          .put(Car.MATURITY_DATE, (carEntry.matDate ?: error("no maturity date")).toEpochMilli())
          .put(Car.PRICE, carEntry.price ?: error("no price"))
          .put(Car.MODEL, carEntry.model ?: error("no model"))
      )
  }

  fun listCars():Single<HttpResponse<Buffer>> =
    client.get(port, host, "/api/car/list")
      .rxSend()

}
