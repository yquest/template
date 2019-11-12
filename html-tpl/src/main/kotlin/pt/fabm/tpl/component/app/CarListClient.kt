package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient

class CarListClient(buffer: Buffer) : CarList(buffer), ClientElement,
  MultiEnvTemplateClient {
  override fun renderImplementation() {
    buffer.appendString("""
    import * as React from "react";
    import { observer } from "mobx-react";
    import { carView } from "../app/controllers/CarViewController";
    import { stores } from "../../stores/Stores";
    """.trimIndent())
    render()
    buffer.appendString("""
    export const CarList = observer(() =>
      stores.carList.cars.length === 0 ? noContent() : content()
    );
    """.trimIndent())
  }

  override fun ifHasCars(block: CarList.() -> Unit) {
    buffer.appendString("const content = () => (")
    block()
    buffer.appendString(");")
  }

  override fun ifHasNoCars(block: CarList.() -> Unit) {
    buffer.appendString("const noContent = () => (")
    block()
    buffer.appendString(");")
  }

  override fun colspanTr(colspan: Int): String = """ colSpan={$colspan}"""

  override fun carLines() {
    buffer.appendString("{carView.carViewList()}")
  }
  override fun showIfAuthenticated(block: CarList.() -> Unit) {
    buffer.appendString("{stores.user.authenticated && (")
    block()
    buffer.appendString(")}")
  }

}
