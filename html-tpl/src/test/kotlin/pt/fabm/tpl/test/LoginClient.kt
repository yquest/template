package pt.fabm.tpl.test

class LoginClient(appendable: Appendable) : Login(appendable) {

  override val attributesBuilder = AttributesBuilderClient()
  override fun asClientText(text: String): String? = text

  override fun modal() {
    TagElement(appendable,"Modal").appendStart().appendEnd()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(appendable,"Notifications").appendStart().appendEnd()
  }

  override fun appInput(label: String, tabIndex: Int, type: String) {
    when (label) {
      "Login" -> appendBody("{React.createElement(AppInput, { ...props.login })}")
      "Password" -> appendBody("{React.createElement(AppInput, { ...props.password })}")
    }
  }

  override fun clientText(text: String) {
    appendBody(text)
  }

  override fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable,"form")
    form.appendStart()
    block()
    form.appendEnd()
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    appendBody("{props.showErrorForm && (")
    block()
    appendBody(")}")
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

}
