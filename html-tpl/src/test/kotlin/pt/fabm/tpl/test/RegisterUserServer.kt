package pt.fabm.tpl.test

class RegisterUserServer(appendable: Appendable, private val auth: Boolean) : RegisterUser(appendable) {
  //ignore
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()

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
      Fields.USERNAME -> appInput.input(type = AppInput.Type.TEXT, value = "", tabIndex = 1)
      Fields.PASSWORD -> appInput.input(type = AppInput.Type.PASSWORD, value = "", tabIndex = 2)
      Fields.EMAIL -> appInput.input(type = AppInput.Type.TEXT, value = "", tabIndex = 3)
    }
  }

  override fun modalBackground() {
    //ignore
  }

  override fun form(onSubmit: String, block: RegisterUser.() -> Unit) {
    val form = TagElement(appendable, "form")
    form.appendStart()
    block()
    form.appendEnd()
  }


}
