package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.AppInputServer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class RegisterUserServer(appendable: Appendable, private val auth: Boolean) : RegisterUser(appendable),
  MultiEnvTemplateServer {
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
    fun inputRender(label: String) {
      appInput.render(label)
    }

    when (label) {
      USERNAME -> inputRender("Username")
      PASSWORD -> inputRender("Password")
      EMAIL -> inputRender("Email")
    }
  }

  override fun modalBackground() {
    //ignore
  }

}
