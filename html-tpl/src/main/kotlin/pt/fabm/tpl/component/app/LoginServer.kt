package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.AppInputServer
import pt.fabm.tpl.component.MultiEnvTemplateServer
import pt.fabm.tpl.component.page.PageInit


class LoginServer(override val auth: Boolean, override val page: Buffer, override val pageInitData: JsonObject) :
  Login(page),
  MultiEnvTemplateServer,
  PageInit {

  override fun asClientText(text: String): String? = null
  override fun literalClassName(value: String): String = ""
  private val fragments = Fragments(buffer)
  override fun modal() {
    //ignore
  }

  override fun navbar() {
    NavBarServer(buffer, auth).render()
  }

  override fun notifications() {
    fragments.serverNotifications()
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    val appInput = AppInputServer(
      appendable = buffer,
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
