package pt.fabm.template.rest.controllers

import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.extensions.nullIfEmpty
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.template.rest.RestResponse
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
      "dao.car.list", null, DeliveryOptions().setCodecName("List")
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
    val model = "model".let { request.getParam(it).nullIfEmpty() ?: RequiredException(it) }

    val jsonFind = JsonObject().put("make", make).put("model", model)
    return vertx.eventBus().rxSend<Car>("dao.car.retrieve", jsonFind)
      .map { message ->
        message.body()?.let {
          RestResponse(it.toJson(), 200)
        } ?: RestResponse(statusCode = 200)
      }
  }

  fun createCar(rc: RoutingContext): Single<RestResponse> {

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
        "dao.car.create", car
      )
      .ignoreElement()
      .toSingle {
        RestResponse(statusCode = 204)
      }
  }
}
