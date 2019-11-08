package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient

class CarListClient(appendable: Appendable) : CarList(appendable), ClientElement,
  MultiEnvTemplateClient {
  override fun renderImplementation() {
    appendable.append("""
    import * as React from "react";
    import { observer } from "mobx-react";
    import { carView } from "../app/controllers/CarViewController";
    import { stores } from "../../stores/Stores";
    """.trimIndent())
    render()
    appendable.append("""
    export const CarList = observer(() =>
      stores.carList.cars.length === 0 ? noContent() : content()
    );
    """.trimIndent())
  }

  override fun ifHasCars(block: CarList.() -> Unit) {
    appendable.append("const content = () => (")
    block()
    appendable.append(");")
  }

  override fun ifHasNoCars(block: CarList.() -> Unit) {
    appendable.append("const noContent = () => (")
    block()
    appendable.append(");")
  }

  override fun colspanTr(colspan: Int): String = """ colSpan={$colspan}"""

  override fun carLines() {
    appendable.append("{carView.carViewList()}")
  }
  override fun showIfAuthenticated(block: CarList.() -> Unit) {
    appendable.append("{stores.user.authenticated && (")
    block()
    appendable.append(")}")
  }

}
