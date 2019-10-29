package pt.fabm.tpl.test

import java.lang.StringBuilder

class LoginClient(appendable: Appendable) : Login(appendable) {
  private val helper = HelperClient()

  override fun asClientText(text: String): String? = text
  override fun start(className: String) {
    root.appendStart(helper.classNameEval(className))
  }

  override fun end() {
    root.appendEnd()
  }

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

  override fun a(onClick: String, block: Login.() -> Unit) {
    val a = TagElement(appendable,"a")
    a.appendStart()
    block()
    a.appendEnd()
  }

  override fun div(className: String?, block: Login.() -> Unit) {
    val a = TagElement(appendable,"div")
    a.appendStart(helper.classNameEval(className))
    block()
    a.appendEnd()
  }

  override fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable,"form")
    form.appendStart()
    block()
    form.appendEnd()
  }

  override fun button(className: String, tabIndex: Int, block: Login.() -> Unit) {
    val button = TagElement(appendable,"button")
    button.appendStart(
      StringBuilder()
        .append(helper.classNameAttr(className))
        .append(helper.tabIndexAttr(tabIndex))
        .toString()
    )
    block()
    button.appendEnd()
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
