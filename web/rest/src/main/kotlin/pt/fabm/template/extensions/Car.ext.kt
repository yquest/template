package pt.fabm.template.extensions

import io.vertx.core.json.JsonObject
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarMake
import java.time.Instant

fun Car.toJson(): JsonObject {
  return JsonObject()
    .put("make", this.make.name)
    .put("model", this.model)
    .put("maturityDate", this.maturityDate.toEpochMilli())
    .put("price", this.price)
}

fun JsonObject.toCar(): Car {
  return Car(
    make = CarMake.valueOf(this.getString("make")),
    model = this.getString("model"),
    maturityDate = Instant.ofEpochMilli(this.getLong("maturityDate")),
    price = this.getInteger("price")
  )
}
