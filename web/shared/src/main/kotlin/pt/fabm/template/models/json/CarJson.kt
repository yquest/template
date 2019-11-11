package pt.fabm.template.models.json

import pt.fabm.template.models.type.Car

data class CarJson(
  val maker: String,
  val model: String,
  val matDate: Long,
  val price: Int
) {

  companion object {
    fun fromCar(car: Car): CarJson {
      return CarJson(
        maker = car.make.name,
        model = car.model,
        price = car.price,
        matDate = car.maturityDate.toEpochMilli()
      )
    }
  }

}
