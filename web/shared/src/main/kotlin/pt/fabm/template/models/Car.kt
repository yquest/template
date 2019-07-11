package pt.fabm.template.models

import java.time.LocalDateTime

data class Car(
  val model: String,
  val make: CarMake,
  val price: Int,
  val maturityDate: LocalDateTime
) {
  companion object FIELDS{
    const val CAR = "car"
    const val MODEL = "model"
    const val MAKE = "make"
    const val PRICE = "price"
    const val MATURITY_DATE = "maturityDate"
  }
  constructor(carId: CarId, price: Int, maturityDate: LocalDateTime) : this(
      model = carId.model,
      make = carId.maker,
      maturityDate = maturityDate,
      price = price
    )
}
