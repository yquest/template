package pt.fabm.tpl.test

class LoginServer(appendable: Appendable, private val auth: Boolean) : Login(appendable) {
  //ignore
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()
  override fun asClientText(text: String): String? = null
  override fun literalClassName(value: String): String = ""

  override fun modal() {
    //ignore
  }

  override fun navbar() {
    NavBar.render { NavBarServer(appendable, auth) }
  }

  override fun notifications() {
    //ignore
  }

  override fun appInput(label: String, tabIndex: Int, type: String) {
    val appInput = AppInputServer(appendable)
    appInput.label { +label }

    when (label) {
      "Login" -> appInput.input(type = AppInput.Type.TEXT, value = "", tabIndex = 1)
      "Password" -> appInput.input(type = AppInput.Type.PASSWORD, value = "", tabIndex = 2)
    }
  }

  override fun clientText(text: String) {
    //ignore
  }

  override fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable, "form")
    form.appendStart()
    block()
    form.appendEnd()
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    //ignore
  }

  override fun modalBackground() {
    //ignore
  }

}
