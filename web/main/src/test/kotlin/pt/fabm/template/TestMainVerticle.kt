package pt.fabm.template

import Consts
import io.jsonwebtoken.Jwts
import io.vertx.core.DeploymentOptions
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.core.json.obj
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.yaml.snakeyaml.Yaml
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.template.models.UserRegisterIn
import java.io.FileReader
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit


@ExtendWith(VertxExtension::class)
class TestMainVerticle {

  var port: Int? = null
  lateinit var host: String

  @BeforeEach
  fun deployVerticle(vertx: Vertx, testContext: VertxTestContext) {
    val configPath = TestMainVerticle::class.java.getResource("/config.yaml").path
    val yaml = Yaml()
    val map: Map<String, Any> = yaml.load(FileReader(configPath))
    map.get("confs").let { it as Map<*, *> }
      .get("rest").let { it as Map<*, *> }
      .also { portAndHost ->
        port = portAndHost.get("port") as Int
        host = portAndHost.get("host") as String
      }

    vertx.rxDeployVerticle(
      MainVerticle(), DeploymentOptions().setConfig(
        JsonObject()
          .put("path", configPath)
      )
    ).subscribe({

      testContext.completeNow()
    }, {
      testContext.failNow(it)
    })
  }

  val digestPass = { pass: String ->
    MessageDigest.getInstance("SHA-512").digest(pass.toByteArray())
  }

  @Test
  @DisplayName("Should call default router")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun serveDefaultHandler(vertx: Vertx, testContext: VertxTestContext) {
    val client = WebClient.create(vertx)
    client.get(port!!, host, "/index.html").send { response ->
      val result = response.result()
      println(result.bodyAsString())
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
    val eventBus = vertx.eventBus()
    val ebConsumer = eventBus
      .consumer<UserRegisterIn>("test.dao.user.create")
      .handler { message ->
        val body = message.body()
        assertEquals(entry["email"], body.email)
        assertEquals(entry["name"], body.name)
        assertArrayEquals(digestPass(entry["password"]), body.pass)
        message.reply(null) // ignored message
      }

    ebConsumer.rxCompletionHandler().subscribe {
      client.post(port!!, host, "/api/user")
        .rxSendJsonObject(entry)
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(200, response.statusCode())
            testContext.completeNow()
          }
        }
    }
  }

  @Test
  @DisplayName("Should login user")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun checkLogin(vertx: Vertx, testContext: VertxTestContext) {

    val client = WebClient.create(vertx)
    val eventBus = vertx.eventBus()
    val entry = jsonObjectOf(
      "user" to "testUser",
      "pass" to "MyPassword"
    )

    val ebConsumer = eventBus
      .consumer<JsonObject>("test.dao.user.login")
      .handler { message ->
        val body = message.body()
        assertEquals(entry.getString("user"), body["user"])
        assertArrayEquals(digestPass(entry["pass"]), body.getBinary("pass"))
        message.reply(true)
      }

    ebConsumer.rxCompletionHandler().subscribe({
      client.post(port!!, host, "/api/user/login")
        .rxSendJsonObject(entry)
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            val cookie = response.cookies().filter { cookieName ->
              cookieName.startsWith(Consts.ACCESS_TOKEN_COOKIE + "=")
            }
            assertEquals(200, response.statusCode())
            assertEquals(1, cookie.size)
            testContext.completeNow()
          }
        }

    }, { error ->
      testContext.failNow(error)
    })
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
    val eventBus = vertx.eventBus()
    val date = LocalDateTime.of(2019, 5, 1,3,6)

    val car = Car("Golf V", CarMake.VOLKSWAGEN, 25000, date)

    val ebConsumer = eventBus
      .consumer<Unit>("test.dao.car.retrieve")
      .handler { message ->
        assertEquals(
          JsonObject().put("model", car.model).put("make", car.make.name),
          message.body()
        )
        message.reply(car)
      }

    ebConsumer.rxCompletionHandler().subscribe({
      client.get(port!!, host, "/api/car")
        .addQueryParam("make", car.make.name)
        .addQueryParam("model", car.model)
        .rxSend()
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(200, response.statusCode())
            assertEquals(
              JsonObject("""{"make":"VOLKSWAGEN","model":"Golf V","maturityDate":"2019-05-01","price":25000}"""),
              response.bodyAsJsonObject()
            )
            testContext.completeNow()
          }
        }
    }, { error ->
      testContext.failNow(error)
    })
  }

  @Test
  @DisplayName("Should throw an response 404")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun carNotFound(vertx: Vertx, testContext: VertxTestContext) {

    val client = WebClient.create(vertx)
    val eventBus = vertx.eventBus()
    val date = LocalDateTime.of(2019, 5, 1,8,9)

    val car = Car("Golf V", CarMake.VOLKSWAGEN, 25000, date)

    val ebConsumer = eventBus
      .consumer<Unit>("test.dao.car.retrieve")
      .handler { message ->
        message.reply(null)
      }

    ebConsumer.rxCompletionHandler().subscribe({
      client.get(port!!, host, "/api/car")
        .addQueryParam("make", car.make.name)
        .addQueryParam("model", car.model)
        .rxSend()
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(404, response.statusCode())
            testContext.completeNow()
          }
        }
    }, { error ->
      testContext.failNow(error)
    })
  }

  @Test
  @DisplayName("Should persist a car")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun createCar(vertx: Vertx, testContext: VertxTestContext) {

    val client = WebClient.create(vertx)
    val eventBus = vertx.eventBus()
    val before1Month = LocalDateTime.now().minusMonths(1)

    val car = Car("Golf V", CarMake.VOLKSWAGEN, 25000, before1Month)

    val jws = Jwts.builder().setSubject("test-user")
      .signWith(Consts.SIGNING_KEY)
      .compact()

    val ebConsumer = eventBus
      .consumer<Unit>("test.dao.car.create")
      .handler { message ->
        assertEquals(car, message.body())
        message.reply(null)
      }

    ebConsumer.rxCompletionHandler().subscribe({
      client.post(port!!, host, "/api/car")
        .putHeader(HttpHeaders.COOKIE.toString(), "${Consts.ACCESS_TOKEN_COOKIE}=$jws")
        .rxSendJsonObject(car.toJson())
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(204, response.statusCode())
            testContext.completeNow()
          }
        }
    }, { error ->
      testContext.failNow(error)
    })
  }

  @Test
  @DisplayName("Should show cars")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun showCars(vertx: Vertx, testContext: VertxTestContext) {

    val client = WebClient.create(vertx)
    val eventBus = vertx.eventBus()
    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      2000,
      LocalDateTime.of(2019, 1, 1,7,8)
    )

    val jws = Jwts.builder().setSubject("test-user")
      .signWith(Consts.SIGNING_KEY)
      .compact()

    val ebConsumer = eventBus
      .consumer<Unit>("test.dao.car.list")
      .handler { message ->
        message.reply(listOf(car), DeliveryOptions().setCodecName("List"))
      }

    ebConsumer.rxCompletionHandler().subscribe({
      client.get(port!!, host, "/api/car/list")
        .putHeader(HttpHeaders.COOKIE.toString(), "${Consts.ACCESS_TOKEN_COOKIE}=$jws")
        .rxSend()
        .subscribe { response: HttpResponse<Buffer> ->
          testContext.verify {
            assertEquals(
              JsonArray(
                """[
                  | {
                  |           "make":"VOLKSWAGEN",
                  |          "model":"Golf V",
                  |   "maturityDate":"2019-01-01",
                  |          "price":2000
                  | }
                  |]""".trimMargin()
              ),
              response.bodyAsJsonArray()
            )
            testContext.completeNow()
          }
        }
    }, { error ->
      testContext.failNow(error)
    })
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

