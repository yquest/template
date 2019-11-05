package pt.fabm.tpl.test

class LoginServer(appendable: Appendable, private val auth: Boolean) : Login(appendable,false) {
  //ignore
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()
  override fun asClientText(text: String): String? = null
  override fun literalClassName(value: String): String = ""

  override fun modal() {
    //ignore
  }

  override fun navbar() {
    NavBarServer(appendable, auth).render()
  }

  override fun notifications() {
    //ignore
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    val appInput = AppInputServer(appendable)
    appInput.label { +label }

    when (label) {
      Fields.LOGIN -> appInput.input(type = AppInput.Type.TEXT, value = "", tabIndex = 1)
      Fields.PASSWORD -> appInput.input(type = AppInput.Type.PASSWORD, value = "", tabIndex = 2)
    }
  }

  override fun clientText(text: String) {
    //ignore
  }

  override fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable, "form")
    form.startStarterTag()
    block()
    form.endTag()
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    //ignore
  }

  override fun modalBackground() {
    //ignore
  }

}
