package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.MultiEnvTemplateServer

class CarViewServer(private val auth: Boolean, appendable: Appendable) : CarView(appendable),
  MultiEnvTemplateServer {

  fun renderServer(cars: List<CarFields>) {
    render(cars)
  }

  override fun showIfAuthenticated(block: CarView.() -> Unit) {
    if (auth) block()
  }

  override fun showIfBlockedRemove(block: CarView.() -> Unit) {
    //ignore
  }

  override fun showIfBlockedNotRemove(block: CarView.() -> Unit) {
    //do always
    block()
  }

  override fun appendClient(text: String) {
    //ignore on server
  }

  override fun appendServer(text: String) {
    appendable.append(text)
  }
}
