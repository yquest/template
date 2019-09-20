package pt.fabm.tpl.component.page

import pt.fabm.tpl.ShowIf

interface PageBlocks {
  companion object text{
    val showLoginButton = "props.appState === app.AppState.LIST_NO_AUTH"
    val showCarEditArea = "props.appState === app.AppState.CAR_EDIT_AUTH"
    val showCarlistArea = "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)";
    val showLoginArea = "props.appState === AppState.LOG_IN_NO_AUTH"
    val showRegisterArea = "props.appState === AppState.REGISTER_NO_AUTH"
    val helloText = """{"Hello "+ props.username + " "}"""
  }
  val logoutArea: ShowIf
  val loginArea: ShowIf
  val carListArea: ShowIf
  val registerArea: ShowIf
}
