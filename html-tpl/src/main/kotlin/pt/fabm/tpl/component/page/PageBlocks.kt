package pt.fabm.tpl.component.page

import pt.fabm.template.models.Car
import pt.fabm.tpl.*
import pt.fabm.tpl.component.car.CarList

interface PageBlocks {
  companion object text {
    val showLoginButton = "props.appState === app.AppState.LIST_NO_AUTH"
    val showCarEditArea = "props.appState === app.AppState.CAR_EDIT_AUTH"
    val showCarlistArea = "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)";
    val showLoginArea = "props.appState === AppState.LOG_IN_NO_AUTH"
    val showRegisterArea = "props.appState === AppState.REGISTER_NO_AUTH"
    val helloText = """{"Hello "+ props.username + " "}"""
  }

  val loginButtonArea: ShowIf
  val logoffArea: ShowIf
  val carListArea: ShowIf
  val registerArea: ShowIf

  //for now only for client
  fun createUserRegistrationArea(type: Type): ElementCreator = Component("UserRegisterEditor", type) {
    AttributeValue.render(type,
      AttributeValue.create { clientAttribute("onReturn", "props.something") },
      AttributeValue.create { clientAttribute("successfullyRegistered", "props.somethingElse") }
    )
  }

  fun createClientCarList(type: Type): ElementCreator = CarList(type)

  fun createCarList(type: Type, carEdit: Boolean = false, list: List<Car> = listOf()): ElementCreator =
    CarList(type, carEdit, list)

  fun createLogoffArea(type: Type, username: () -> String = { error("not expected") }): ElementCreator = DIV(type) {
    AttributeValue.render(type,
      AttributeValue.create { className("float-right") }
    )
  }.apply {
    +({ "Hello ${username()} " } to """{"Hello "+ props.username + " "}""")
    a(href = "", onClick = "props.loginOff") {
      +"logoff"
      i(className = "fas fa-sign-out-alt")
    }
  }

  fun createLoginButton(type: Type): ElementCreator = A(type, mutableListOf()) {
    AttributeValue.render(type,
      AttributeValue.create { className("float-right") },
      AttributeValue.create { clientAttribute("onClick", "props.loginOn") }
    )
  }.apply {
    +"Sign in"
    i(className = "fas fa-sign-in-alt")
  }
}
