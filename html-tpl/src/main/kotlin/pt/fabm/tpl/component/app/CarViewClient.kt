package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient

class CarViewClient(buffer: Buffer) : CarView(buffer), ClientElement,
  MultiEnvTemplateClient {

  fun render() {
    render(
      listOf(
        CarFields(
          maker = "{props.maker}",
          model = "{props.model}",
          price = "{props.price}",
          matDate = "{props.maturityDate}"
        )
      )
    )
  }

  override fun showIfAuthenticated(block: CarView.() -> Unit) {
    buffer.appendString("""{props.authenticated && (""")
    block()
    buffer.appendString(")}")
  }

  override fun showIfBlockedRemove(block: CarView.() -> Unit) {
    buffer.appendString("""{props.blockedRemove &&(""")
    block()
    buffer.appendString(")}")
  }

  override fun showIfBlockedNotRemove(block: CarView.() -> Unit) {
    buffer.appendString("""{!props.blockedRemove &&(""")
    block()
    buffer.appendString(")}")
  }

  override fun renderImplementation() {
    buffer.appendString("""
    import * as React from "react";
    import { carView } from "../app/controllers/CarViewController";
    
    export const CarView = (props: carView.Props) => (
    """.trimIndent())
    render()
    buffer.appendString(");")
  }

  override fun appendClient(text: String) {
    buffer.appendString(text)
  }

  override fun appendServer(text: String) {
    //ignore on client
  }

}
