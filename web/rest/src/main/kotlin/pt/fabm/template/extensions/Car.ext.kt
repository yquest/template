package pt.fabm.template.extensions

import io.vertx.core.json.JsonObject
import pt.fabm.template.models.Car
import java.time.format.DateTimeFormatter

fun Car.toJson():JsonObject{
  return JsonObject()
    .put("make",this.make.name)
    .put("model",this.model)
    .put("maturityDate",this.maturityDate.toEpochMilli())
    .put("price",this.price)
}
