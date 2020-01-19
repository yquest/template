package pt.fabm.template.rest.controllers

import io.reactivex.Single
import io.vertx.core.json.JsonArray
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ViewsController(private val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(ViewsController::class.java)
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

  fun main(rc: RoutingContext) {

    fun Instant.toAppFormat(): String {
      val formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd, kk:mm")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())

      return formatter.format(this)
    }

    fun render(auth: Boolean, list: List<Car>): ViewPage {
      val buffer = Buffer.buffer()
      val app = AppServer(
        buffer = buffer.delegate, auth = auth, cars = list.map {
          CarFields(
            maker = it.make.label,
            model = it.model,
            matDate = it.maturityDate.toAppFormat(),
            price = "${it.price}€"
          )
        },
        pageInitData = jsonObjectOf(
          "cars" to list.map {
            jsonObjectOf(
              "make" to it.make.ordinal,
              "model" to it.model,
              "maturityDate" to it.maturityDate.toEpochMilli(),
              "price" to it.price
            )
          }.let { JsonArray(it) },
          "auth" to auth
        )
      )
      return ViewPage(app)
    }

    val authRx = createAuthRx(rc)

    fun onSuccess(buffer: Buffer) = rc.response().end(buffer)
    fun onError(error: Throwable) {
      rc.response().statusCode = 503
      LOGGER.error(error)
      rc.response().end()
    }

    vertx.eventBus()
      .rxSend<List<Car>>(EventBusAddresses.Dao.Car.list, null)
      .map { it.body() }
      .flatMap { list ->
        authRx.map { isAuth ->
          Buffer(
            render(isAuth, list).render()
          )
        }
      }.subscribe(::onSuccess, ::onError)
  }
}
