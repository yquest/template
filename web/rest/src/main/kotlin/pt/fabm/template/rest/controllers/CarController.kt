package pt.fabm.template.rest.controllers

import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.json.JsonArray
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.eventbus.Message
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.nullIfEmpty
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarId
import pt.fabm.template.models.type.CarMake
import pt.fabm.template.rest.AuthorizationHandler
import pt.fabm.template.rest.RestResponse
import pt.fabm.template.validation.AuthException
import pt.fabm.template.validation.InvalidEntryException
import pt.fabm.template.validation.RequiredException

class CarController(val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(CarController::class.java)!!
  }

  fun authChecker(rc: RoutingContext): Maybe<RoutingContext> {
    return rc.user().rxIsAuthorized("global").flatMapMaybe {
      if (it) return@flatMapMaybe Maybe.just(rc)
      else return@flatMapMaybe Maybe.empty<RoutingContext>()
    }
  }

  fun carList(): Single<RestResponse> {

    fun messageSent(message: Message<List<Car>>): RestResponse {
      val list = message.body()
      val jsonArray = JsonArray()
      for (car in list) {
        jsonArray.add(car.toJson())
      }
      return RestResponse(jsonArray, 200)
    }

    return vertx.eventBus().rxSend<List<Car>>(
      EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
    ).map(::messageSent)
  }

  fun getCar(rc: RoutingContext) {

    fun createCarId(): CarId {
      val request = rc.request()
      val make = request.getParam(CarId.MAKE)
      val model = request.getParam(CarId.MODEL)

      if(make.isNullOrEmpty()) throw RequiredException(CarId.MAKE)
      if(model.isNullOrEmpty()) throw RequiredException(CarId.MODEL)

      return CarId(
        make = try {
          CarMake.values()[make.toInt()]
        } catch (e: NumberFormatException) {
          throw InvalidEntryException(make, CarId.MAKE)
        },
        model = model
      )
    }

    fun receivedMessage(message:Message<Car?>):Car? = message.body()

    fun handleError(error: Throwable): RestResponse {
      if (error !is ReplyException || error.failureCode() != 1) {
        LOGGER.error("error on event bus ${EventBusAddresses.Dao.Car.retrieve}", error)
        return RestResponse(statusCode = 500)
      }
      return RestResponse(statusCode = 404)
    }

    Single.fromCallable { createCarId() }
      .flatMap { vertx.eventBus().rxSend<Car>(EventBusAddresses.Dao.Car.retrieve,it) }

    vertx.eventBus().rxSend<Car>(EventBusAddresses.Dao.Car.retrieve,createCarId())
      .map(::receivedMessage).doOnSuccess { LOGGER.trace("receive message:$it") }

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
