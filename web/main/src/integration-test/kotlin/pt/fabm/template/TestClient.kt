package pt.fabm.template

import Consts
import io.jsonwebtoken.Jwts
import io.netty.handler.codec.http.cookie.ClientCookieDecoder
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.core.json.obj
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.client.HttpRequest
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
import org.apache.http.impl.cookie.BasicClientCookie
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.yaml.snakeyaml.Yaml
import pt.fabm.template.dao.DaoMemoryShared
import pt.fabm.template.extensions.toHash
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.template.models.UserRegisterIn
import pt.fabm.template.validation.AuthException
import java.io.FileReader
import java.nio.file.Paths
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.time.chrono.ChronoLocalDate
import java.time.chrono.Chronology
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalUnit
import java.util.concurrent.TimeUnit


@ExtendWith(VertxExtension::class)
class TestClient {

  private val port: Int = 8888
  private val host: String = "localhost"
  private val user: String = "TestUser"
  private val password: String= "MyPassword"

  fun createUser(vertx: Vertx, testContext: VertxTestContext) {

    val entry = json {
      obj(
        "name" to user,
        "email" to "my@email.com",
        "password" to password,
        "token" to "123"
      )
    }

    val client = WebClient.create(vertx)
    client.post(port, host, "/api/user")
      .rxSendJsonObject(entry)
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(204, response.statusCode())
          testContext.completeNow()
        }
      }
  }

  private fun login(client: WebClient, onSuccess: (HttpResponse<Buffer>) -> Unit = {}) {

    val entry = jsonObjectOf(
      "user" to user,
      "pass" to password
    )

    client.post(port, host, "/api/user/login")
      .rxSendJsonObject(entry)
      .subscribe { response: HttpResponse<Buffer> ->
        onSuccess(response)
      }
  }

  @Test
  @DisplayName("Should login user")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun checkLogin(vertx: Vertx, testContext: VertxTestContext) {

    createUser(vertx,testContext)

    val client = WebClient.create(vertx)
    login(client) { response ->
      testContext.verify {
        val cookie = response.cookies().filter { cookieName ->
          cookieName.startsWith(Consts.ACCESS_TOKEN_COOKIE + "=")
        }
        assertEquals(200, response.statusCode())
        assertEquals(1, cookie.size)
        testContext.completeNow()
      }
    }
  }

  @Test
  @DisplayName("Should persist a car")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun createCar(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    val before1Month = Instant.now().minus(1,ChronoUnit.MONTHS)

    val car = Car("Golf VI", CarMake.VOLKSWAGEN, 25000, before1Month)

    login(client) {clientResp->
      client.post(port, host, "/api/car")
        .auth(clientResp)
        .rxSendJsonObject(car.toJson())
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(204, response.statusCode())
            testContext.completeNow()
          }
        }
    }
  }

  @Test
  @DisplayName("Should persist a car after 2 seconds login")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun createCarReplaceToken(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    val before1Month = Instant.now().minus(1,ChronoUnit.MONTHS)

    val car = Car("Golf V", CarMake.VOLKSWAGEN, 25000, before1Month)

    fun createCar(clientResponse: HttpResponse<*>) {
      client.post(port, host, "/api/car")
        .auth(clientResponse)
        .rxSendJsonObject(car.toJson())
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(car, DaoMemoryShared.cars[0])
            assertEquals(204, response.statusCode())
            testContext.completeNow()
          }
        }
    }

    login(client) { response ->
      vertx.setTimer(4000) { createCar(response) }
    }
  }

  @Test
  @DisplayName("Should show cars")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun showCars(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    DaoMemoryShared.cars.add(
      Car(
        "Golf V",
        CarMake.VOLKSWAGEN,
        2000,
        LocalDateTime.of(2019, 1, 1, 7, 8).toInstant(ZoneOffset.UTC)
      )
    )

    client.get(port, host, "/api/car/list")
      .rxSend()
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(
            JsonArray(
              """[
                  | {
                  |           "make":"VOLKSWAGEN",
                  |          "model":"Golf V",
                  |   "maturityDate":"2019-01-01T07:08:00",
                  |          "price":2000
                  | }
                  |]""".trimMargin()
            ),
            response.bodyAsJsonArray()
          )
          testContext.completeNow()
        }
      }
  }

}

/**
 * forward cookies from server
 */
private fun HttpRequest<Buffer>.auth(resp: HttpResponse<*>): HttpRequest<Buffer> {
  val value = resp.cookies().find { cookie -> cookie.startsWith(Consts.ACCESS_TOKEN_COOKIE) }
    .let { it ?: throw AuthException() }
  return this.putHeader(HttpHeaders.COOKIE.toString(), value)
}

@Suppress("UNCHECKED_CAST")
private fun <V> Map<String, *>.getTypedValue(key: String): V = this[key] as V
