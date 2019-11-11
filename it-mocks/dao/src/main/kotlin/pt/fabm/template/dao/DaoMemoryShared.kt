package pt.fabm.template.dao

import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.UserRegisterIn

object DaoMemoryShared {
  lateinit var users:MutableMap<String, UserRegisterIn>
  lateinit var cars:MutableList<Car>
}
