package pt.fabm.template.rest.controllers

import io.reactivex.Single
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

class ViewsController(private val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(ViewsController::class.java)
  }

  fun main(rc: RoutingContext) {
    val claims = Single.fromCallable {
      AuthorizationHandler.getClaims(rc).claims
    }.cache()

    fun render(auth: Boolean, list: List<Car>): ViewPage {
      val buffer = Buffer.buffer()
      val app = AppServer(
        buffer = buffer.delegate, auth = auth, cars = list.map {
          CarFields(
            maker = it.make.label,
            model = it.model,
            matDate = it.maturityDate.toString(),
            price = it.price.toString()
          )
        },
        pageInitData = jsonObjectOf()
      )
      return ViewPage(app)
    }

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
        claims.map { true }.onErrorReturn { false }.map { isAuth ->
          Buffer(
            render(isAuth, list).render()
          )
        }
      }.subscribe(::onSuccess, ::onError)
  }
}
