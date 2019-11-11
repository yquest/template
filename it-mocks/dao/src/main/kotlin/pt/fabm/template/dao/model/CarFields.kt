package pt.fabm.template.dao.model

import io.vertx.core.json.JsonObject
import pt.fabm.template.models.type.Car

data class CarFields(
  val maker: Int,
  val model: String,
  val maturityDate: Long,
  val price: Int
) {
  companion object {
    fun fromJson(json: JsonObject) {
      CarFields(
        maker = json.getInteger(Car.MAKE),
        model = json.getString(Car.MODEL),
        maturityDate = json.getLong(Car.MATURITY_DATE),
        price = json.getInteger(Car.PRICE)
      )
    }

    fun fromCar(car: Car): CarFields =
      CarFields(
        maker = car.make.ordinal,
        model = car.model,
        maturityDate = car.maturityDate.toEpochMilli(),
        price = car.price
      )

  }

  fun toJson(): JsonObject = JsonObject()
    .put(Car.MAKE, maker)
    .put(Car.MODEL, model)
    .put(Car.MATURITY_DATE, maturityDate)
    .put(Car.PRICE, price)
}
