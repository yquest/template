package pt.fabm.template.rest.controllers

import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.nullIfEmpty
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarId
import pt.fabm.template.models.CarMake
import pt.fabm.template.rest.RestResponse
import pt.fabm.template.validation.DataAlreadyExists
import pt.fabm.template.validation.InvalidEntryException
import pt.fabm.template.validation.RequiredException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CarController(val vertx: Vertx) {
  companion object {
    val LOGGER = LoggerFactory.getLogger(CarController::class.java)
  }

  fun carList(): Single<RestResponse> {
    return vertx.eventBus().rxSend<List<Car>>(
      EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
    )
      .map { message -> message.body() }
      .flatMapObservable { list -> Observable.fromIterable(list) }
      .map { car ->
        JsonObject()
          .put("make", car.make.name)
          .put("model", car.model)
          .put("maturityDate", car.maturityDate.toString())
          .put("price", car.price)
      }
      .collect({ JsonArray() }, { jsonArray, element -> jsonArray.add(element) })
      .map { RestResponse(it, 200) }
  }

  fun getCar(rc: RoutingContext): Single<RestResponse> {
    val request = rc.request()
    val make = "make".let {
      val current = request.getParam(it).nullIfEmpty() ?: throw RequiredException(it)
      CarMake.values().find { it.name == current } ?: throw InvalidEntryException(current, it)
    }
    val model: String = "model".let { request.getParam(it).nullIfEmpty() ?: throw RequiredException(it) }

    return vertx.eventBus().rxSend<Car>(EventBusAddresses.Dao.Car.retrieve, CarId(model = model, maker = make))
      .map { message ->
        message.body()?.let {
          RestResponse(it.toJson(), 200)
        } ?: RestResponse(statusCode = 404)
      }
  }

  fun createOrUpdateCar(createAction: Boolean, rc: RoutingContext): Single<RestResponse> {

    val rootKey = "car"
    val car = rc.bodyAsJson?.let { body ->
      val notEmptyString: (String) -> String? = {
        body.getString(it).nullIfEmpty()
      }

      val strModel = "model".let { notEmptyString(it) ?: throw RequiredException("$rootKey.$it") }
      val strMake = "make".let { notEmptyString(it) ?: throw RequiredException("$rootKey.$it") }
      val price = "price".let { body.getInteger(it) ?: throw RequiredException("$rootKey.$it") }
      val strMaturityDate = "maturityDate".let { notEmptyString(it) ?: throw RequiredException("$rootKey.$it") }

      return@let Car(
        strModel,
        CarMake.values().find {
          it.name.equals(strMake)
        } ?: throw InvalidEntryException(strMake, "$rootKey.make"),
        price,
        LocalDateTime.parse(strMaturityDate, DateTimeFormatter.ISO_DATE_TIME)
      )
    } ?: throw RequiredException(rootKey)
    return vertx.eventBus()
      .rxSend<Unit>(
        if (createAction) EventBusAddresses.Dao.Car.create else EventBusAddresses.Dao.Car.update, car
      ).onErrorReturn { e ->
        if (e is ReplyException)
          if (e.failureCode() == 1)
            throw DataAlreadyExists()
        throw IllegalStateException()
      }
      .ignoreElement()
      .toSingle {
        RestResponse(statusCode = if (createAction) 204 else 200)
      }
  }

  fun deleteCar(rc: RoutingContext): Single<RestResponse> {
    val request = rc.request()
    val model = request.getParam("model")
    val make = request.getParam("make")

    return vertx.eventBus().rxSend<Unit>(
      EventBusAddresses.Dao.Car.delete, CarId(model = model, maker = CarMake.valueOf(make))
    ).ignoreElement()
      .toSingle {
        RestResponse(statusCode = 200)
      }
  }
}
