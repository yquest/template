package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.AppInputServer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class LoginServer(appendable: Appendable, private val auth: Boolean) : Login(appendable),
  MultiEnvTemplateServer {

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
    val appInput = AppInputServer(
      appendable = appendable,
      type = type,
      tabIndex = tabIndex,
      value = ""
    )
    fun renderInput(label: String) {
      appInput.render(label)
    }
    when (label) {
      LOGIN -> renderInput("Login")
      PASSWORD -> renderInput("Password")
    }
  }

  override fun clientText(text: String) {
    //ignore
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    //ignore
  }

  override fun modalBackground() {
    //ignore
  }

  override fun appendClient(text: String) {
    //ignore
  }

  override fun appendServer(text: String) {
    appendBody(text)
  }

}
