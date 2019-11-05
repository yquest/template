package pt.fabm.tpl.test

class NavBarServer(appendable: Appendable, private val auth:Boolean) : NavBar(appendable) {
  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    if(auth) block()
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    if(!auth) block()
  }

  override fun appendClient(text: String) {
    //ignore
  }

  override fun appendServer(text: String) {
    appendBody(text)
  }
}
