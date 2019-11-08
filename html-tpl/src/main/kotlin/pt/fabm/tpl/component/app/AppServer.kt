package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.MultiEnvTemplateServer

class AppServer(appendable: Appendable, private val auth: Boolean, private val cars: List<CarFields>) :
  App(appendable), MultiEnvTemplateServer {
  override fun modal() {
    //ignore
  }

  override fun navBar() {
    NavBarServer(appendable, auth).render()
  }

  override fun notifications() {
    //ignore
  }

  override fun carList() {
    CarListServer(appendable, auth, cars).render()
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
