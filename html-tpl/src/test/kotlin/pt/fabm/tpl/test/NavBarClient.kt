package pt.fabm.tpl.test

class NavBarClient(appendable: Appendable) : NavBar(appendable) {
  private val root = TagElement(appendable, "div")
  override fun start(className: String) {
    appendable.append("""
      import * as React from "react";
      import { stores } from "../../stores/Stores";
      import { observer } from "mobx-react";
      import { navbar } from "../app/controllers/NavbarController";
      
      export const Navbar = observer((props: navbar.Props) => (
    """.trimIndent())
    root.appendStart(""" className="$className"""")
  }

  override fun end() {
    root.appendEnd()
    appendable.append("));");
  }

  override fun i(className: String) {
    val i = TagElement(appendable, "i")
    i.appendStart(""" class="$className"""").appendEnd()
  }

  override fun h5(className: String, onClick: String, block: TagElement.() -> Unit) {
    val h5 = TagElement(appendable, "h5")
    h5.appendStart(""" className="$className" onClick={$onClick}""")
    h5.block()
    h5.appendEnd()
  }

  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    appendable.append("""{stores.user.authenticated && (""")
    block()
    appendable.append(""")}""")
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    appendable.append("""{!stores.user.authenticated && (""")
    block()
    appendable.append(""")}""")
  }

  override fun a(className: String?, onClick: String, block: TagElement.() -> Unit) {
    val a = TagElement(appendable, "a")
    a.appendStart(""" href="" ${if(className==null) "" else """ className="$className"""" } onClick={$onClick}""")
    a.block()
    a.appendEnd()
  }

}
