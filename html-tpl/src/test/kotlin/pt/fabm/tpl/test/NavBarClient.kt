package pt.fabm.tpl.test

class NavBarClient(appendable: Appendable) : NavBar(appendable),ClientElement {

  override fun renderImplementation(){
    appendBody("""
      import * as React from "react";
      import { stores } from "../../stores/Stores";
      import { observer } from "mobx-react";
      import { navbar } from "../app/controllers/NavbarController";
      
      export const Navbar = observer((props: navbar.Props) => (
    """.trimIndent())
    render()
    appendBody("));")
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

  override fun appendClient(text: String) {
    appendBody(text)
  }

  override fun appendServer(text: String) {
    //ignore
  }

}
