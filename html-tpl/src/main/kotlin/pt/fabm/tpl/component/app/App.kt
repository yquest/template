package pt.fabm.tpl.component.app

import pt.fabm.template.models.Car
import pt.fabm.tpl.*
import pt.fabm.tpl.component.car.CarList

class App(
  override val type: Type,
  var auth: Boolean = false,
  var carEdit: Boolean = false,
  var carList: List<Car> = emptyList(),
  var username: ()->String = {error("unexpected username entry")}
) : ElementCreator {

  override fun create(): Element {
    val appStateNoAuth = { !auth } to "props.appState === app.AppState.LIST_NO_AUTH"
    val carEditAuth = { carEdit } to "props.appState === app.AppState.CAR_EDIT_AUTH"
    val listAuthAndCarEdit = { !auth || carEdit } to "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)"
    val username: Pair<() -> String, String> = { "Hello ${username()} " } to "Hello {props.username + \" \"}"

    fun createContainerApp(type: Type, init: DIV.() -> Unit): DIV {
      val div = DIV(type) {
        AttributeValue.render(type, AttributeValue.create {
          className("container app")
        })
      }
      div.init()
      return div
    }

    val containerApp = createContainerApp(type) {
      fun showIf(clause: Pair<() -> Boolean, String>, init: ShowIf.() -> Unit): ShowIf {
        val showIf = ShowIf(clause, type)
        showIf.init()
        this.children += showIf
        return showIf
      }
      showIf(appStateNoAuth) {
        a(href = "javascript:void();", onClick = "props.loginOn") {
          +"Sign in"
          i(className = "fas fa-sign-in-alt")
        }
      }
      children += Notifications(type.toFirstLevel())
      showIf(carEditAuth) {
        div(className = "float-right", key = "helloUsername") {
          +username
          a(href = "javascript:void(0)", onClick = "props.loginOff") {
            +"logoff"
            i(className = "fas fa-sign-out-alt")
          }
        }
      }
      showIf(listAuthAndCarEdit) {
        component(CarList(type = type.toFirstLevel(), carEdit = carEdit, list = carList))
      }
    }
    return containerApp.create()
  }
}
