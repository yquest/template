package pt.fabm.tpl.test

abstract class Login(appendable: Appendable) : Element(appendable) {
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
                appInput(label = Fields.LOGIN, tabIndex = 1, type = AppInput.Type.TEXT)
                appInput(label = Fields.PASSWORD, tabIndex = 2, type = AppInput.Type.PASSWORD)
                a(onClick = "props.onClickRegisterLink") { +"Create User" }
                button(className = "btn btn-primary col-sm-12", tabIndex = 3) { +"Login" }
              }
              showIfErrorForm {
                div(attr = literalClassName("props.errorFormClasses")) { clientText("{props.errorForm}") }
              }
            }
          }
        }
      }
      modalBackground()
    }
  }

  internal abstract val attributesBuilder: AttributesBuilder
  abstract fun modal()
  abstract fun navbar()
  abstract fun notifications()
  abstract fun appInput(label: String, tabIndex: Int, type: AppInput.Type = AppInput.Type.TEXT)
  abstract fun literalClassName(value: String): String

  fun a(onClick: String, block: Login.() -> Unit) {
    val a = TagElement(appendable,"a")
      .startStarterTag(
        attributesBuilder
          .emptyHref()
          .onClickAttr(onClick)
          .build()
      )
    this.block()
    a.endTag()
  }

  fun div(className: String? = null, attr: String? = null, block: Login.() -> Unit) {

    val a = TagElement(appendable, "div")
      .startStarterTag(attributesBuilder.classNameEval(className).append(attr ?: "").build())
    this.block()
    a.endTag()
  }

  fun button(className: String, tabIndex: Int, block: Login.() -> Unit) {
    val button = TagElement(appendable, "button")
      .startStarterTag(    attributesBuilder
        .classNameAttr(className)
        .tabIndexAttr(tabIndex)
        .build()
      )
    this.block()
    button.endTag()
  }

  abstract fun form(onSubmit: String, block: Login.() -> Unit)
  abstract fun showIfErrorForm(block: Login.() -> Unit)
  abstract fun modalBackground()
  abstract fun clientText(text: String)
  abstract fun asClientText(text: String): String?
}
