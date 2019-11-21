package pt.fabm

import io.reactivex.Single
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpRequest
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.UserRegisterIn

class Client(vertx: Vertx) {
  private val client = WebClient.create(vertx)
  private val port = 8888
  private val host = "localhost"

  fun createUser(user: User): Single<HttpResponse<Buffer>> {
    return client.post(port, host, "/api/user")
      .rxSendJsonObject(
        JsonObject()
          .put(UserRegisterIn.NAME, user.name ?: error("no name"))
          .put(UserRegisterIn.PASS, (user.pass ?: error("no pass")).toByteArray())
          .put(UserRegisterIn.EMAIL, user.email ?: error("no email"))
      )
  }

  fun createUser(init: User.() -> Unit): Single<HttpResponse<Buffer>> {
    val user = User()
    user.init()
    return createUser(user)
  }

  fun login(user: User): Single<String> {
    return client.post(port, host, "/api/user/login")
      .rxSendJsonObject(
        JsonObject()
          .put("user", user.name ?: error("no name"))
          .put(
            UserRegisterIn.PASS, (user.pass ?: error("no pass"))
              .toByteArray()
          )
      ).map { response ->
        response.cookies().find { it.startsWith("access_token") }.toString()
      }
  }


  fun login(init: User.() -> Unit): Single<String> {
    val user = User()
    user.init()
    return login(user)
  }

  fun createCar(token: String, carEntry: CarEntry): Single<HttpResponse<Buffer>> {
    return client.post(port, host, "/api/car")
      .putHeader(HttpHeaders.COOKIE.toString(), token)
      .rxSendJsonObject(
        JsonObject()
          .put(Car.MAKE, (carEntry.maker ?: error("no make")).ordinal)
          .put(Car.MATURITY_DATE, (carEntry.matDate ?: error("no maturity date")).toEpochMilli())
          .put(Car.PRICE, carEntry.price ?: error("no price"))
          .put(Car.MODEL, carEntry.model ?: error("no model"))
      )
  }

  fun createCar(token: String, init: CarEntry.() -> Unit): Single<HttpResponse<Buffer>> {
    val carEntry = CarEntry()
    carEntry.init()
    return createCar(token, carEntry)
  }

  fun listCars(): Single<JsonArray> =
    client.get(port, host, "/api/car/list")
      .rxSend().map { it.bodyAsJsonArray() }

  fun mainPage(token: String? = null): Single<HttpResponse<Buffer>> {
    val http: HttpRequest<Buffer> = client.get(port, host, "/").let { req ->
      if (token == null) req
      else req.putHeader(HttpHeaders.COOKIE.toString(), token)
    }

    return http.rxSend()
  }

}
