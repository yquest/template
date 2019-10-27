package pt.fabm.tpl.test

class AppServer(appendable: Appendable, private val auth:Boolean, private val cars:List<CarFields>) : App(appendable) {
  private val rootDiv = TagElement(appendable, "div")
  override fun start(className: String) {
    rootDiv.appendStart(""" class="$className"""")
  }

  override fun end() {
    rootDiv.appendEnd()
  }

  override fun button(className: String, tabindex: Int, onClick: String, block: TagElement.() -> Unit) {
    val button = TagElement(appendable, "button")
    button.appendStart(""" class="$className" tabIndex="$tabindex"""")
    button.block()
    button.appendEnd()
  }

  override fun modal() {
    //ignore
  }

  override fun navBar() {
    NavBar.render { NavBarServer(appendable, auth) }
  }

  override fun notifications() {
    //ignore
  }

  override fun carList() {
    CarList.render { CarListServer(appendable, auth, cars) }
  }

  override fun showIfAuthenticated(block: App.() -> Unit) {
    if(auth)block()
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
