package pt.fabm.template

import Consts
import io.jsonwebtoken.Jwts
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
import io.vertx.reactivex.ext.web.client.HttpRequest
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
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
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarMake
import pt.fabm.template.models.type.UserRegisterIn
import pt.fabm.template.validation.AuthException
import java.io.FileReader
import java.nio.file.Paths
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit


@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  private var port: Int? = null
  private lateinit var host: String

  @BeforeEach
  fun deployVerticle(vertx: Vertx, testContext: VertxTestContext) {
    port = 8888
    val configUri = TestMainVerticle::class.java.getResource("/config.yaml").toURI()
    val configPath = Paths.get(configUri)
      .parent.toAbsolutePath().toFile().absolutePath
    System.setProperty("conf", configPath)
    System.setProperty("server.port", port.toString())

    val yaml = Yaml()

    host = yaml.load<Map<String, Any>>(FileReader(Paths.get(configUri).toFile()))
      .getTypedValue<Map<String, Any>>("confs")
      .getTypedValue<Map<String, Any>>("rest")
      .getTypedValue("host")

    vertx.rxDeployVerticle(MainVerticle()).subscribe({
      testContext.completeNow()
    }, {
      testContext.failNow(it)
    })
  }

  @Test
  @DisplayName("Should call default router")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun serveDefaultHandler(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    client.get(port!!, host, "/index.html").send { response ->
      val result = response.result()
      testContext.verify {
        assertEquals(200, result.statusCode())
        testContext.completeNow()
      }
    }
  }

  @Test
  @DisplayName("Should create an user")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun createUser(vertx: Vertx, testContext: VertxTestContext) {

    val digestPass = { pass: String ->
      MessageDigest.getInstance("SHA-512").digest(pass.toByteArray())
    }

    val entry = json {
      obj(
        "name" to "testName",
        "email" to "my@email.com",
        "password" to "myPassword",
        "token" to "123"
      )
    }

    val client = WebClient.create(vertx)
    client.post(port!!, host, "/api/user")
      .rxSendJsonObject(entry)
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(204, response.statusCode())
          val users = DaoMemoryShared.users

          Assert.assertEquals(1, users.size)
          val expectedUserRegister = users["testName"]
          Assert.assertEquals("testName", expectedUserRegister?.name)
          Assert.assertTrue(digestPass("myPassword")!!.contentEquals(expectedUserRegister!!.pass))
          Assert.assertEquals("my@email.com", expectedUserRegister.email)
          testContext.completeNow()
        }
      }
  }

  private fun login(client: WebClient, onSuccess: (HttpResponse<Buffer>) -> Unit = {}) {

    DaoMemoryShared.users["testUser"] = UserRegisterIn(
      name = "testUser",
      email = "ignore",
      pass = "MyPassword".toHash()
    )

    val entry = jsonObjectOf(
      "user" to "testUser",
      "pass" to "MyPassword"
    )

    client.post(port!!, host, "/api/user/login")
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
  @DisplayName("Should fail on authentication when tries to show the reservation")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun authenticationFailedShowReservation(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    client.post(port!!, host, "/api/car")
      .putHeader(HttpHeaders.COOKIE.toString(), "${Consts.ACCESS_TOKEN_COOKIE}=aaa.bbb.ccc")
      .rxSend()
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          response.bodyAsJsonObject().also { jsonObject ->
            assertEquals(jsonObjectOf("error" to "Autentication Fails"), jsonObject)
          }
          assertEquals(401, response.statusCode())
          testContext.completeNow()
        }
      }
  }

  @Test
  @DisplayName("Should show a car")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun showCar(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    val date = LocalDateTime.of(2019, 5, 1, 3, 6)

    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      25000,
      date.toInstant(ZoneOffset.UTC)
    )
    DaoMemoryShared.cars.add(car)

    client.get(port!!, host, "/api/car")
      .addQueryParam(Car.MAKE, car.make.name)
      .addQueryParam(Car.MODEL, car.model)
      .rxSend()
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(200, response.statusCode())
          assertEquals(
            JsonObject(
              """{
                  |"make":"VOLKSWAGEN",
                  |"model":"Golf V",
                  |"maturityDate":1556679960000,
                  |"price":25000}"""
                .trimMargin()
            ),
            response.bodyAsJsonObject()
          )
          testContext.completeNow()
        }
      }
  }

  @Test
  @DisplayName("Should update a car")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun updateCar(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    val date1 = LocalDateTime.of(2019, 5, 1, 8, 9)
    val date2 = LocalDateTime.of(2019, 5, 1, 8, 10)

    val car1 = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      25000,
      date1.toInstant(ZoneOffset.UTC)
    )
    val car2 = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      2000,
      date2.toInstant(ZoneOffset.UTC)
    )

    DaoMemoryShared.cars.add(car1)

    login(client) { clientResp ->
      client.put(port!!, host, "/api/car")
        .auth(clientResp)
        .rxSendJsonObject(car2.toJson())
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(200, response.statusCode())
            assertEquals(car2, DaoMemoryShared.cars[0])
            testContext.completeNow()
          }
        }
    }
  }


  @Test
  @DisplayName("Should throw an response 404")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun carNotFound(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    val date = LocalDateTime.of(2019, 5, 1, 8, 9)

    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      25000,
      date.toInstant(ZoneOffset.UTC)
    )

    client.get(port!!, host, "/api/car")
      .addQueryParam(Car.MAKE, car.make.name)
      .addQueryParam(Car.MODEL, car.model)
      .rxSend()
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(404, response.statusCode())
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
    val before1Month = LocalDateTime.now().minusMonths(1)

    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      25000,
      before1Month.toInstant(ZoneOffset.UTC)
    )


    login(client) { clientResp ->

      client.post(port!!, host, "/api/car")
        .auth(clientResp)
        .rxSendJsonObject(car.toJson())
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(car, DaoMemoryShared.cars[0])
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
    val before1Month = LocalDateTime.now().minusMonths(1)

    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      25000,
      before1Month.toInstant(ZoneOffset.UTC)
    )

    fun createCar(clientResponse: HttpResponse<*>) {
      client.post(port!!, host, "/api/car")
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

    client.get(port!!, host, "/api/car/list")
      .rxSend()
      .subscribe { response: HttpResponse<Buffer> ->
        testContext.verify {
          assertEquals(
            JsonArray(
              """[
                  | {
                  |           "make":"VOLKSWAGEN",
                  |          "model":"Golf V",
                  |   "maturityDate":1546326480000,
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

  @Test
  @DisplayName("Should generate a token")
  fun jwtTest() {
    val username = "user-name"
    val jwsToken = Jwts.builder().setSubject(username).addClaims(
      mapOf("role" to "watcher")
    ).signWith(Consts.SIGNING_KEY).compact()

    val claims = Jwts.parser().requireSubject(username)
      .setSigningKey(Consts.SIGNING_KEY)
      .parseClaimsJws(jwsToken)

    assertEquals("watcher", claims.body["role"])
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
