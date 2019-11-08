package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class Login(appendable: Appendable) : Element(appendable), MultiEnvTemplate {
  companion object Fields {
    const val LOGIN = "Login"
    const val PASSWORD = "Password"
  }

  fun render() {
    div(className = "container app") {
      modal()
      navbar()
      notifications()
      div {
        div(className = "row justify-content-sm-center") { +"Login" }
        div(className = "row justify-content-sm-center") {
          div(className = "card col-sm-6 col-lg-4") {
            div(className = "card-body") {
              form(onSubmit = "props.submitForm") {
                appInput(label = LOGIN, tabIndex = 1, type = AppInput.Type.TEXT)
                appInput(label = PASSWORD, tabIndex = 2, type = AppInput.Type.PASSWORD)
                a(onClick = "props.onClickRegisterLink") { +"Create User" }
                button(className = "btn btn-primary col-sm-12", tabIndex = 3) { +"Login" }
              }
              showIfErrorForm {
                div(clientClass = "props.errorFormClasses") { clientText("{props.errorForm}") }
              }
            }
          }
        }
      }
      modalBackground()
    }
  }

  abstract fun modal()
  abstract fun navbar()
  abstract fun notifications()
  abstract fun appInput(label: String, tabIndex: Int, type: AppInput.Type = AppInput.Type.TEXT)
  abstract fun literalClassName(value: String): String

  fun a(onClick: String, block: Login.() -> Unit) {
    val a = TagElement(appendable, "a")
      .startStarterTag()
    //attributes
    appendClient(" onClick={$onClick}")

    a.endStarterTag()
    this.block()
    a.endTag()
  }

  fun div(clientClass: String? = null, className: String? = null, block: Login.() -> Unit) {

    val a = TagElement(appendable, "div")
      .startStarterTag()

    //attributes
    if (className != null) appendClassName(className)
    appendClient(" className={$clientClass}")

    a.endStarterTag()
    this.block()
    a.endTag()
  }

  fun button(className: String, tabIndex: Int, block: Login.() -> Unit) {
    val button = TagElement(appendable, "button")
      .startStarterTag()
    //attributes
    appendClassName(className)
    appendClient(" tabIndex={$tabIndex}")
    appendServer(""" tabindex="$tabIndex"""")

    button.endStarterTag()
    this.block()
    button.endTag()
  }
  fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable,"form")
    appendClient(" onSubmit={$onSubmit}")
    this.block()
    form.endTag()
  }
  abstract fun showIfErrorForm(block: Login.() -> Unit)
  abstract fun modalBackground()
  abstract fun clientText(text: String)
  abstract fun asClientText(text: String): String?
}
