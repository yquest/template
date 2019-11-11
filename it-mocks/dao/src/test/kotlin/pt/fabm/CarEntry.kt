package pt.fabm

import pt.fabm.template.models.type.CarMake
import java.time.Instant

data class CarEntry(
  var model: String? = null,
  var maker: CarMake? = null,
  var matDate: Instant? = null,
  var price: Int? = null
)
