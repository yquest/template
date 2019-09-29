package pt.fabm.tpl.component.page

import pt.fabm.template.models.Car
import pt.fabm.tpl.ElementCreator
import pt.fabm.tpl.NameElementCreator
import pt.fabm.tpl.ShowIf
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.car.CarList

class App(
  type: Type,
  auth: Boolean = false,
  carEdit: Boolean = false,
  var carList: List<Car> = emptyList(),
  username: () -> String = { error("unexpected username entry") }
) : Page(type, auth, carEdit, username) {
  override fun createComponent1(): ElementCreator {
    val listAuthAndCarEdit = { !auth || carEdit } to "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)"

    return ShowIf(listAuthAndCarEdit, type).apply {
      children += CarList(type = type.toFirstLevel(), carEdit = carEdit, list = carList)
    }
  }

  override fun createComponent2(): ElementCreator =
    ShowIf("app.carStore.selectedCar !== null", type).apply {
      children += NameElementCreator("CarEditor", type) {
        """ title={props.carEditProps.title}"""+
        """ maturityDate={props.carEditProps.maturityDate}"""+
        """ onSubmit={props.carEditProps.onSubmit}"""
      }
    }
}
