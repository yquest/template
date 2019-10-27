package pt.fabm.tpl.test

class NavBarServer(appendable: Appendable, private val auth:Boolean) : NavBar(appendable) {
  private val root = TagElement(appendable, "div")
  override fun start(className: String) {
    root.appendStart(""" class="$className"""")
  }

  override fun end() {
    root.appendEnd()
  }

  override fun h5(className: String, onClick: String, block: TagElement.() -> Unit) {
    val h5 = TagElement(appendable, "h5")
    h5.appendStart(""" class="$className"""")
    h5.block()
    h5.appendEnd()
  }

  override fun i(className: String) {
    val i = TagElement(appendable, "i")
    i.appendStart(""" class="$className"""").appendEnd()
  }

  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    if(auth) block()
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    if(!auth) block()
  }

  override fun a(className: String?, onClick: String, block: TagElement.() -> Unit) {
    val a = TagElement(appendable, "a")
    a.appendStart(""" href="" ${if(className==null) "" else """ class="$className"""" }""")
    a.block()
    a.appendEnd()
  }

}
