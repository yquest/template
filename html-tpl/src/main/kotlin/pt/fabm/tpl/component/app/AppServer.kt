package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import pt.fabm.tpl.component.MultiEnvTemplateServer
import pt.fabm.tpl.component.page.PageInit

class AppServer(
  buffer: Buffer = Buffer.buffer(),
  override val auth: Boolean,
  private val cars: List<CarFields>,
  override val pageInitData: JsonObject
) :
  App(buffer), MultiEnvTemplateServer, PageInit {
  override val page: Buffer get() = buffer

  override fun modal() {
    //ignore
  }

  override fun navBar() {
    NavBarServer(buffer, auth).render()
  }

  override fun notifications() {
    //ignore
  }

  override fun carList() {
    CarListServer(buffer, auth, cars).render()
  }

  override fun showIfAuthenticated(block: App.() -> Unit) {
    if (auth) block()
  }

  override fun showIfEditableAndAuthenticated(block: App.() -> Unit) {
    //always ignored
  }

  override fun carEditor() {
    //always ignored
  }

  override fun modalBackground() {
    //always ignored
  }
}
