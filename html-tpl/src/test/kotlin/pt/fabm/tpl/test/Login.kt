package pt.fabm.tpl.test

abstract class Login(appendable: Appendable) : Element(appendable) {

  companion object {
    fun render(loginCreator: () -> Login) {
      fun login(className: String, block: Login.() -> Unit) {
        val loginElement = loginCreator()
        loginElement.start(className)
        loginElement.block()
        loginElement.end()
      }

      login(className = "container app") {
        modal()
        navbar()
        notifications()
        div {
          div(className = "row justify-content-sm-center") { +"Login" }
          div(className = "row justify-content-sm-center") {
            div(className = "card col-sm-6 col-lg-4") {
              div(className = "card-body") {
                form(onSubmit = "props.submitForm") {
                  appInput(label = "Login", tabIndex = 1, type = "TEXT")
                  appInput(label = "Password", tabIndex = 2, type = "PASSWORD")
                  a(onClick = "") { +"Create User" }
                  button(className = "btn btn-primary col-sm-12", tabIndex = 3) { +"Login" }
                }
                showIfErrorForm {
                  div(className = asClientText("props.errorFormClasses")) { clientText("{props.errorForm}") }
                }
              }
            }
          }
        }
        modalBackground()
      }
    }
  }

  protected val root = TagElement(appendable, "div")
  abstract fun start(className: String)
  abstract fun end()
  abstract fun modal()
  abstract fun navbar()
  abstract fun notifications()
  abstract fun appInput(label: String, tabIndex: Int, type: String = "TEXT")
  abstract fun a(onClick: String, block: Login.() -> Unit)
  abstract fun div(className: String? = null, block: Login.() -> Unit)
  abstract fun form(onSubmit: String, block: Login.() -> Unit)
  abstract fun button(className: String, tabIndex: Int, block: Login.() -> Unit)
  abstract fun showIfErrorForm(block: Login.() -> Unit)
  abstract fun modalBackground()
  abstract fun clientText(text: String)
  abstract fun asClientText(text: String): String?
}
