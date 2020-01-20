package pt.fabm.template.rest.controllers

import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.models.type.Car
import pt.fabm.template.rest.AuthorizationHandler
import pt.fabm.template.rest.ViewPage
import pt.fabm.tpl.component.app.AppServer
import pt.fabm.tpl.component.app.CarFields
import pt.fabm.tpl.component.app.LoginServer
import pt.fabm.tpl.component.page.PageInit
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ViewsController(private val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(ViewsController::class.java)

    private fun Instant.toAppFormat(): String {
      val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd, kk:mm")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

      return formatter.format(this)
    }

    private fun carToCarFields(car: Car): CarFields = CarFields(
      maker = car.make.label,
      model = car.model,
      matDate = car.maturityDate.toAppFormat(),
      price = "${car.price}€"
    )

    private fun carToJson(car: Car): JsonObject =
      jsonObjectOf(
        Car.MAKE to car.make.ordinal,
        Car.MODEL to car.model,
        Car.MATURITY_DATE to car.maturityDate.toEpochMilli(),
        Car.PRICE to car.price
      )
  }

  private fun createAuthRx(rc: RoutingContext): Single<Boolean> {
    val claims = Single.fromCallable {
      AuthorizationHandler.getClaims(rc).claims
    }.cache()
    return claims.map { true }.onErrorReturn { false }
  }


  fun login(rc: RoutingContext) {
    fun onSuccess(buffer: Buffer) = rc.response().end(buffer)
    fun onError(error: Throwable) {
      rc.response().statusCode = 503
      LOGGER.error(error)
      rc.response().end()
    }

    val buffer = Buffer.buffer()
    val authRx = createAuthRx(rc)
    authRx.map { auth ->
      LoginServer(
        auth = auth,
        page = buffer.delegate,
        pageInitData = jsonObjectOf(
          "cars" to JsonArray(),
          "page" to "login",
          "auth" to auth
        )
      )
    }.map {
      Buffer(ViewPage(it).render())
    }.subscribe(::onSuccess, ::onError)

  }

  fun onError(rc: RoutingContext): (error: Throwable) -> Unit {
    return { error ->
      rc.response().statusCode = 503
      LOGGER.error(error)
      rc.response().end()
    }
  }

  fun onSuccess(rc: RoutingContext): (buffer: Buffer) -> Unit {
    return { buffer ->
      rc.response().end(buffer)
    }
  }

  fun main(rc: RoutingContext) {

    fun render(auth: Boolean, list: List<Car>): PageInit {
      val buffer = Buffer.buffer()
      val app = AppServer(
        buffer = buffer.delegate, auth = auth, cars = list.map(::carToCarFields),
        pageInitData = jsonObjectOf(
          "cars" to JsonArray(list.map(::carToJson)),
          "auth" to auth
        )
      )
      return app
    }

    createAuthRx(rc).flatMap { auth ->
      doAppServer { list ->
        render(auth, list)
      }
    }.subscribe(onSuccess(rc), onError(rc))
  }

  private fun doAppServer(viewPageLoader: (List<Car>) -> PageInit): Single<Buffer> {
    return vertx.eventBus()
      .rxSend<List<Car>>(EventBusAddresses.Dao.Car.list, null)
      .map { it.body() }
      .map { list ->
        Buffer(
          ViewPage(
            viewPageLoader(list)
          ).render()
        )
      }
  }

  fun mainNoSSR(rc: RoutingContext) {
    fun createPageInit(auth: Boolean, list: List<Car>): PageInit {
      val pageInit: PageInit = object : PageInit {
        override val pageInitData: JsonObject = jsonObjectOf(
          "auth" to auth,
          "cars" to JsonArray(list.map(::carToJson))
        )
        override val auth: Boolean = auth
        override val page: io.vertx.core.buffer.Buffer = io.vertx.core.buffer.Buffer.buffer()
        override fun render() {
          //do nothing
        }
      }
      return pageInit
    }

    createAuthRx(rc).flatMap { auth ->
      doAppServer { list ->
        createPageInit(auth, list)
      }
    }.subscribe(onSuccess(rc), onError(rc))
  }
}
