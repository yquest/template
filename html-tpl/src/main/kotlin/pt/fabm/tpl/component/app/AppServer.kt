package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class AppServer(buffer: Buffer, private val auth: Boolean, private val cars: List<CarFields>) :
  App(buffer), MultiEnvTemplateServer {
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
