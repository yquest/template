package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient

class CarViewClient(appendable: Appendable) : CarView(appendable), ClientElement,
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
    appendable.append("""{props.authenticated && (""")
    block()
    appendable.append(")}")
  }

  override fun showIfBlockedRemove(block: CarView.() -> Unit) {
    appendable.append("""{props.blockedRemove &&(""")
    block()
    appendable.append(")}")
  }

  override fun showIfBlockedNotRemove(block: CarView.() -> Unit) {
    appendable.append("""{!props.blockedRemove &&(""")
    block()
    appendable.append(")}")
  }

  override fun renderImplementation() {
    appendable.append("""
    import * as React from "react";
    import { carView } from "../app/controllers/CarViewController";
    
    export const CarView = (props: carView.Props) => (
    """.trimIndent())
    render()
    appendable.append(");")
  }

  override fun appendClient(text: String) {
    appendable.append(text)
  }

  override fun appendServer(text: String) {
    //ignore on client
  }

}
