package pt.fabm.tpl.test

class LoginServer(appendable: Appendable, private val auth: Boolean) : Login(appendable) {
  override fun asClientText(text: String): String? = null
  override fun start(className: String) {
    root.appendStart(""" class="$className"""")
  }

  override fun end() {
    root.appendEnd()
  }

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
      "Login" -> {
        appInput.input(type = AppInput.Type.TEXT.label, value = "", tabIndex = 1)
      }
      "Password" -> {
        appInput.input(type = AppInput.Type.PASSWORD.label, value = "", tabIndex = 2)
      }
    }
  }

  override fun clientText(text: String) {
    //ignore
  }

  override fun a(onClick: String, block: Login.() -> Unit) {
    val a = TagElement(appendable,"a")
    a.appendStart()
    block()
    a.appendEnd()
  }

  override fun div(className: String?, block: Login.() -> Unit) {
    val a = TagElement(appendable,"div")
    fun checkedClassName():String = if(className == null) "" else """ class="$className""""
    a.appendStart(checkedClassName())
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
    button.appendStart(""" class="$className" tabindex="$tabIndex"""")
    block()
    button.appendEnd()
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    //ignore
  }

  override fun modalBackground() {
    //ignore
  }

}
