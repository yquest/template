package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient

class NavBarClient(appendable: Buffer) : NavBar(appendable), ClientElement,
  MultiEnvTemplateClient {

  override fun renderImplementation() {
    appendBody(
      """
      import * as React from "react";
      import { stores } from "../../stores/Stores";
      import { observer } from "mobx-react";
      import { navbar } from "../app/controllers/NavbarController";
      
      export const Navbar = observer((props: navbar.Props) => (
    """.trimIndent()
    )
    render()
    appendBody("));")
  }

  override fun showIfAuthenticated(block: NavBar.() -> Unit) {
    buffer.appendString("""{stores.user.authenticated && (""")
    block()
    buffer.appendString(""")}""")
  }

  override fun showIfNotAuthenticated(block: NavBar.() -> Unit) {
    buffer.appendString("""{!stores.user.authenticated && (""")
    block()
    buffer.appendString(""")}""")
  }

  override fun appendClient(text: String) {
    appendBody(text)
  }

  override fun appendServer(text: String) {
    //ignore
  }

}
