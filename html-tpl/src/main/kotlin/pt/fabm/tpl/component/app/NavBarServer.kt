package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.MultiEnvTemplateServer

class NavBarServer(buffer: Buffer, private val auth:Boolean) : NavBar(buffer),
  MultiEnvTemplateServer {
  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    if(auth) block()
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    if(!auth) block()
  }

}
