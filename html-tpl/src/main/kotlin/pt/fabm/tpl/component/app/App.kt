package pt.fabm.tpl.component.app

import pt.fabm.template.models.Car
import pt.fabm.tpl.Component
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.car.CarList

class App(
  type: Type,
  auth: Boolean = false,
  carEdit: Boolean = false,
  var carList: List<Car> = emptyList(),
  username: () -> String = { error("unexpected username entry") }
) : Page(type, auth, carEdit, username) {
  override fun createComponent(): Component =
    CarList(type = type.toFirstLevel(), carEdit = carEdit, list = carList)
}
