package pt.fabm.template.rest.controllers

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
import pt.fabm.template.extensions.toEnum
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarId
import pt.fabm.template.models.type.CarMake
import pt.fabm.template.rest.RestResponse
import pt.fabm.template.validation.InvalidEntryException
import pt.fabm.template.validation.RequiredException
import java.time.Instant

class CarController(val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(CarController::class.java)!!
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

  fun getCar(rc: RoutingContext): Single<RestResponse> {
    val request = rc.request()

    val make = request.getParam(Car.MAKE).nullIfEmpty()
      .let { it ?: throw RequiredException(Car.MAKE) }
      .let { (it toEnum CarMake::class.java) ?: throw InvalidEntryException(it, Car.MAKE) }

    val model = request.getParam(Car.MODEL).nullIfEmpty() ?: throw RequiredException(Car.MODEL)

    fun sentMessage(message: Message<Car>): RestResponse {
      val body = message.body()
      return if (body == null) RestResponse(statusCode = 404)
      else RestResponse(body.toJson(), statusCode = 200)
    }

    fun handleError(error: Throwable): RestResponse {
      if (error !is ReplyException || error.failureCode() != 1) {
        LOGGER.error("error on event bus ${EventBusAddresses.Dao.Car.retrieve}", error)
        return RestResponse(statusCode = 500)
      }
      return RestResponse(statusCode = 404)
    }

    return vertx.eventBus().rxSend<Car>(EventBusAddresses.Dao.Car.retrieve,
      CarId(model = model, maker = make)
    )
      .map(::sentMessage)
      .onErrorReturn(::handleError)
  }

  fun createOrUpdateCar(createAction: Boolean, rc: RoutingContext): Single<RestResponse> {
    val body = rc.bodyAsJson
    if (rc.bodyAsJson == null) throw RequiredException(Car.CAR)

    fun lbCar(lb: String) = "${Car.CAR}.$lb"

    val car = Car(
      model = body.getString(Car.MODEL).nullIfEmpty()
        ?: throw RequiredException(lbCar(Car.MODEL)),
      make = body.getString(Car.MAKE).nullIfEmpty()
        .let { it ?: throw RequiredException(lbCar(Car.MAKE)) }
        .let { strMake ->
          val make = strMake toEnum CarMake::class.java
          make ?: throw InvalidEntryException(strMake, lbCar(Car.MAKE))
        },
      price = body.getInteger(Car.PRICE)
        ?: throw RequiredException(lbCar(Car.PRICE)),
      maturityDate = body.getLong(Car.MATURITY_DATE).let { Instant.ofEpochMilli(it) }
    )

    val ebAddress = if (createAction) EventBusAddresses.Dao.Car.create
    else EventBusAddresses.Dao.Car.update

    fun handleError(error: Throwable): RestResponse {
      if (error !is ReplyException || error.failureCode() != 1) {
        LOGGER.error("error on event bus $ebAddress", error)
        return RestResponse(statusCode = 500)
      }
      return RestResponse(statusCode = 400)
    }

    return vertx.eventBus().rxSend<Unit>(ebAddress, car)
      .ignoreElement()
      .toSingle {
        RestResponse(statusCode = if (createAction) 204 else 200)
      }
      .onErrorReturn(::handleError)
  }

  fun deleteCar(rc: RoutingContext): Single<RestResponse> {
    val request = rc.request()
    val carId = CarId(
      model = request.getParam(Car.MODEL).nullIfEmpty()
        ?: throw RequiredException(Car.MODEL),
      maker = request.getParam(Car.MAKE).nullIfEmpty()
        .let { it ?: throw RequiredException(Car.MAKE) }
        .let { strMake ->
          val make = strMake toEnum CarMake::class.java
          make ?: throw InvalidEntryException(strMake, Car.MAKE)
        }
    )
    return vertx.eventBus().rxSend<Unit>(EventBusAddresses.Dao.Car.delete, carId)
      .ignoreElement()
      .toSingle {
        RestResponse(statusCode = 200)
      }
  }
}
