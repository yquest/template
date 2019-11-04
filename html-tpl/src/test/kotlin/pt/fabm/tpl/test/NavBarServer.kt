package pt.fabm.tpl.test

class NavBarServer(appendable: Appendable, private val auth:Boolean) : NavBar(appendable) {
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()

  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    if(auth) block()
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    if(!auth) block()
  }
}
