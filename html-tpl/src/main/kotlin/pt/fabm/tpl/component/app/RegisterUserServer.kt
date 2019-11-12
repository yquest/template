package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.AppInputServer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class RegisterUserServer(buffer: Buffer, private val auth: Boolean) : RegisterUser(buffer),
  MultiEnvTemplateServer {
  override fun modal() {
    //ignore
  }

  override fun navbar() {
    NavBarServer(buffer, auth).render()
  }

  override fun notifications() {
    //ignore
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    val appInput = AppInputServer(
      appendable = buffer,
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
