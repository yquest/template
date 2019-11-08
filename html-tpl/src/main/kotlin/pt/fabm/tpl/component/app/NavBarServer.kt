package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.MultiEnvTemplateServer

class NavBarServer(appendable: Appendable, private val auth:Boolean) : NavBar(appendable),
  MultiEnvTemplateServer {
  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    if(auth) block()
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    if(!auth) block()
  }

}
