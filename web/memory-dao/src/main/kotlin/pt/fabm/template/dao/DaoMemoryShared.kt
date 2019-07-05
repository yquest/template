package pt.fabm.template.dao

import pt.fabm.template.models.Car
import pt.fabm.template.models.UserRegisterIn

object DaoMemoryShared {
  lateinit var users:MutableMap<String,UserRegisterIn>
  lateinit var cars:MutableList<Car>
}
