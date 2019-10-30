package pt.fabm.tpl.test

abstract class Login(appendable: Appendable) : Element(appendable) {

  companion object {
    fun render(loginCreator: () -> Login) {
      fun login(className: String, block: Login.() -> Unit) {
        val loginElement = loginCreator()
        loginElement.attributesBuilder.builder.clear()
        loginElement.attributesBuilder.classNameAttr(className)
        loginElement.root.appendStart(loginElement.attributesBuilder.builder.toString())
        loginElement.block()
        loginElement.root.appendEnd()
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
  }

  internal val root = TagElement(appendable, "div")
  abstract val attributesBuilder: AttributesBuilder
  abstract fun modal()
  abstract fun navbar()
  abstract fun notifications()
  abstract fun appInput(label: String, tabIndex: Int, type: String = "TEXT")
  abstract fun literalClassName(value:String):String
  fun a(onClick: String, block: Login.() -> Unit) {
    attributesBuilder.builder.clear()
    attributesBuilder.emptyHref()
    attributesBuilder.onClickAttr(onClick)
    val a = TagElement(appendable, "a")
      .appendStart(attributesBuilder.builder.toString())
    this.block()
    a.appendEnd()
  }

  fun div(className: String? = null, attr: String? = null, block: Login.() -> Unit) {
    attributesBuilder.builder.clear()
    attributesBuilder.classNameEval(className)
    attributesBuilder.builder.append(attr ?: "")
    val a = TagElement(appendable, "div")
      .appendStart(attributesBuilder.builder.toString())
    this.block()
    a.appendEnd()
  }

  fun button(className: String, tabIndex: Int, block: Login.() -> Unit) {
    attributesBuilder.builder.clear()
    attributesBuilder
      .classNameAttr(className)
      .tabIndexAttr(tabIndex)
    val button = TagElement(appendable, "button")
      .appendStart(attributesBuilder.builder.toString())
    this.block()
    button.appendEnd()
  }

  abstract fun form(onSubmit: String, block: Login.() -> Unit)
  abstract fun showIfErrorForm(block: Login.() -> Unit)
  abstract fun modalBackground()
  abstract fun clientText(text: String)
  abstract fun asClientText(text: String): String?
}
