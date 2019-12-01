package pt.fabm.template.rest.controllers

import io.reactivex.Single
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.json.JsonArray
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarId
import pt.fabm.template.models.type.CarMake
import pt.fabm.template.rest.AuthorizationHandler
import pt.fabm.template.validation.AuthException
import pt.fabm.template.validation.InvalidEntryException
import pt.fabm.template.validation.RequiredException

class CarController(val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(CarController::class.java)!!
  }

  fun carList(rc: RoutingContext) {

    fun onSuccess(carList: List<Car>) {
      val buffer = JsonArray(carList.map { it.toJson() })
        .toBuffer()
        .let { Buffer(it) }
      rc.response().end(buffer)
    }

    fun onError(error: Throwable) {
      rc.response().statusCode = 503
      rc.response().end()
    }

    vertx.eventBus().rxSend<List<Car>>(
      EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
    ).map { it.body() }
      .subscribe(::onSuccess, ::onError)
  }

  fun getCar(rc: RoutingContext) {

    fun createCarId(): CarId {
      val request = rc.request()
      val make = request.getParam(CarId.MAKE)
      val model = request.getParam(CarId.MODEL)

      if (make.isNullOrEmpty()) throw RequiredException(CarId.MAKE)
      if (model.isNullOrEmpty()) throw RequiredException(CarId.MODEL)

      return CarId(
        make = try {
          CarMake.values()[make.toInt()]
        } catch (e: NumberFormatException) {
          throw InvalidEntryException(make, CarId.MAKE)
        },
        model = model
      )
    }

    fun onError(error: Throwable) {
      if (error is ReplyException && error.failureCode() == 1) {
        rc.response().statusCode = 404
        rc.response().end()
      } else {
        LOGGER.error("error on event bus ${EventBusAddresses.Dao.Car.retrieve}", error)
        rc.response().statusCode = 500
        rc.response().end()
      }
    }

    fun onSuccess(car: Car) {
      rc.response()
        .end(car.toJsonObject().toBuffer().let { Buffer(it) })
    }

    Single.fromCallable(::createCarId)
      .flatMap { vertx.eventBus().rxSend<Car>(EventBusAddresses.Dao.Car.retrieve, it) }
      .map { it.body() }
      .subscribe(::onSuccess, ::onError)

  }

  fun createCar(rc: RoutingContext) = createOrUpdateCar(true, rc)
  fun updateCar(rc: RoutingContext) = createOrUpdateCar(false, rc)

  private fun createOrUpdateCar(createAction: Boolean, rc: RoutingContext) {
    val car = Single.fromCallable {
      AuthorizationHandler.getClaims(rc).claims
      Car.fromBasicJson(rc.bodyAsJson)
    }

    val ebAddress = if (createAction) EventBusAddresses.Dao.Car.create
    else EventBusAddresses.Dao.Car.update

    val response = rc.response()
    fun handleError(error: Throwable) {
      if (error !is ReplyException || error.failureCode() != 1) {
        LOGGER.error("error on event bus $ebAddress", error)
        response.statusCode = 500
      } else if (error is AuthException) response.statusCode = 403
      else response.statusCode = 400
      response.end()
    }

    fun creationConfirm() {
      response.statusCode = 204
      response.end()
    }

    car
      .flatMap {
        vertx.eventBus().rxSend<Unit>(ebAddress, it)
      }.ignoreElement()
      .subscribe(::creationConfirm, ::handleError)

  }

  fun deleteCar(rc: RoutingContext) {

    val ebAddress = EventBusAddresses.Dao.Car.delete
    val response = rc.response()
    fun handleError(error: Throwable) {
      if (error !is ReplyException || error.failureCode() != 1) {
        LOGGER.error("error on event bus $ebAddress", error)
        response.statusCode = 500
      } else if (error is AuthException) response.statusCode = 403
      else response.statusCode = 400
      response.end()
    }

    fun confirmDelete() {
      rc.response().end()
    }

    val carId = Single.fromCallable {
      AuthorizationHandler.getClaims(rc).claims
      CarId.fromBasicJson(rc.bodyAsJson)
    }

    carId
      .flatMap {
        vertx.eventBus().rxSend<Unit>(EventBusAddresses.Dao.Car.delete, it)
      }.ignoreElement()
      .subscribe(::confirmDelete, ::handleError)

  }

}
