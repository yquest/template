package pt.fabm.template.models.type

import io.vertx.core.json.JsonObject
import java.time.Instant

data class Car(
  val model: String,
  val make: CarMake,
  val price: Int,
  val maturityDate: Instant
) {
  companion object FIELDS {
    fun fromBasicJson(jsonObject: JsonObject): Car = Car(
      model = jsonObject.getString(MODEL),
      price = jsonObject.getInteger(PRICE),
      make = CarMake.values()[jsonObject.getInteger(MAKE)],
      maturityDate = Instant.ofEpochMilli(jsonObject.getLong(MATURITY_DATE))
    )

    const val CAR = "car"
    const val MODEL = "model"
    const val MAKE = "make"
    const val PRICE = "price"
    const val MATURITY_DATE = "maturityDate"
  }

  constructor(carId: CarId, price: Int, maturityDate: Instant) : this(
    model = carId.model,
    make = carId.make,
    maturityDate = maturityDate,
    price = price
  )

  fun toJsonObject(): JsonObject = JsonObject()
    .put(MODEL, model)
    .put(PRICE, price)
    .put(MATURITY_DATE, maturityDate.epochSecond)
    .put(MAKE, make.ordinal)
}
