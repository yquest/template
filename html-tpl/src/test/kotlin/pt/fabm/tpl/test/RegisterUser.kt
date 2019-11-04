package pt.fabm.tpl.test

abstract class RegisterUser(appendable: Appendable, isClient: Boolean) : Element(isClient,appendable) {

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
                appInput(label = Fields.USERNAME, tabIndex = 1, type = AppInput.Type.TEXT)
                appInput(label = Fields.PASSWORD, tabIndex = 2, type = AppInput.Type.PASSWORD)
                appInput(label = Fields.EMAIL, tabIndex = 3, type = AppInput.Type.TEXT)
                button(className = "btn btn-primary col-sm-12", tabIndex = 4) { +"Register User" }
              }
            }
          }
        }
      }
      modalBackground()
    }
  }

  abstract val attributesBuilder: AttributesBuilder
  abstract fun modal()
  abstract fun navbar()
  abstract fun notifications()
  abstract fun appInput(label: String, tabIndex: Int, type: AppInput.Type = AppInput.Type.TEXT)

  fun div(className: String? = null, attr: String? = null, block: RegisterUser.() -> Unit) {

    val a = TagElement(appendable,isClient, "div")
      .startStarterTag(
        attributesBuilder
          .classNameEval(className)
          .append(attr ?: "")
          .build()
      )
    this.block()
    a.endTag()
  }

  fun button(className: String, tabIndex: Int, block: RegisterUser.() -> Unit) {

    val button = TagElement(appendable,isClient, "button")
      .startStarterTag(
        attributesBuilder
          .classNameAttr(className)
          .tabIndexAttr(tabIndex)
          .build()
      )
    this.block()
    button.endTag()
  }

  abstract fun form(onSubmit: String, block: RegisterUser.() -> Unit)
  abstract fun modalBackground()
}
