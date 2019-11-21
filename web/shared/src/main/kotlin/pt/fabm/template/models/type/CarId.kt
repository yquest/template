package pt.fabm.template.models.type

import io.vertx.core.json.JsonObject

data class CarId(
  val model: String,
  val make: CarMake
) {
  companion object FIELDS {
    fun fromBasicJson(jsonObject: JsonObject): CarId = CarId(
      model = jsonObject.getString(MODEL),
      make = CarMake.values()[jsonObject.getInteger(MAKE)]
    )

    const val CAR_ID = "carId"
    const val MODEL = "model"
    const val MAKE = "make"
  }

  constructor(carId: CarId) : this(
    model = carId.model,
    make = carId.make
  )

  fun toJsonObject(): JsonObject = JsonObject()
    .put(MODEL, model)
    .put(MAKE, make.ordinal)
}
