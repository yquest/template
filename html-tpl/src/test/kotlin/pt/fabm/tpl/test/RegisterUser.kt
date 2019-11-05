package pt.fabm.tpl.test

abstract class RegisterUser(appendable: Appendable) : Element(appendable),MultiEnvTemplate {

  companion object Fields {
    const val USERNAME = "Username"
    const val PASSWORD = "Password"
    const val EMAIL = "Email"
  }

  fun render() {
    div(className = "container app") {
      modal()
      navbar()
      notifications()
      div {
        div(className = "row justify-content-sm-center") { +"User Register" }
        div(className = "row justify-content-sm-center") {
          div(className = "card col-sm-6 col-lg-4") {
            div(className = "card-body") {
              form(onSubmit = "props.submitForm") {
                appInput(label = USERNAME, tabIndex = 1, type = AppInput.Type.TEXT)
                appInput(label = PASSWORD, tabIndex = 2, type = AppInput.Type.PASSWORD)
                appInput(label = EMAIL, tabIndex = 3, type = AppInput.Type.TEXT)
                button(className = "btn btn-primary col-sm-12", tabIndex = 4) { +"Register User" }
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

  fun div(className: String? = null, block: RegisterUser.() -> Unit) {
    val a = TagElement(appendable, "div")
      .startStarterTag()
    //attributes
    if(className != null) appendClassName(className)

    a.endStarterTag()
    this.block()
    a.endTag()
  }

  fun button(className: String, tabIndex: Int, block: RegisterUser.() -> Unit) {
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

  fun form(onSubmit: String, block: RegisterUser.() -> Unit) {
    val form = TagElement(appendable,"form")
    form.startStarterTag()
    //attributes
    appendClient(" onSubmit={$onSubmit}")

    form.endStarterTag()
    this.block()
    form.endTag()
  }

  abstract fun modalBackground()
}
