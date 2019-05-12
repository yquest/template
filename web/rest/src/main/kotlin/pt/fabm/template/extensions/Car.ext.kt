package pt.fabm.template.extensions

import io.vertx.core.json.JsonObject
import pt.fabm.template.models.Car
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Car.toJson():JsonObject{
  return JsonObject()
    .put("make",this.make)
    .put("model",this.model)
    .put("maturityDate",this.maturityDate.format(DateTimeFormatter.ISO_DATE))
    .put("price",this.price)
}
