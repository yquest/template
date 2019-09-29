package pt.fabm.tpl.component.page

import pt.fabm.tpl.*
import pt.fabm.tpl.component.app.NavBar
import pt.fabm.tpl.component.app.Notifications

abstract class Page(
  override val type: Type,
  var auth: Boolean = false,
  var carEdit: Boolean = false,
  var username: () -> String = { error("unexpected username entry") }
) : ElementCreator {

  override fun create(): Element {
    val appStateNoAuth = { !auth } to "props.appState === app.AppState.LIST_NO_AUTH"
    val carEditAuth = { carEdit } to "props.appState === app.AppState.CAR_EDIT_AUTH"
    val listAuthAndCarEdit = { !auth || carEdit } to "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)"

    val containerApp = DIV(type) {
      AttributeValue.render(type,
        AttributeValue.create {
          className("container app")
        })
    }

    containerApp.apply {
      component(Component("Modal", type.toFirstLevel()))
      children += NavBar(type.toFirstLevel(), carEditAuth.first, listAuthAndCarEdit.first)
      children += ShowIf(appStateNoAuth, type).apply {
        a(href = "", onClick = "props.loginOn") {
          +"Sign in"
          i(className = "fas fa-sign-in-alt")
        }
      }
      children += Notifications(type.toFirstLevel())
      children += ShowIf(carEditAuth, type).apply {
        div(className = "float-right")
      }
      children += createComponent1()
      children += createComponent2()
      //modal overlay, show only on client
      children += ShowIf({ false } to "uiStore.modelInDOM", type).apply {
        div(className = "modal-backdrop fade show")
      }
    }
    return containerApp.create()
  }

  abstract fun createComponent1(): ElementCreator
  abstract fun createComponent2(): ElementCreator
}
