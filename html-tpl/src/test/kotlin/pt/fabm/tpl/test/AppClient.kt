package pt.fabm.tpl.test

import java.lang.StringBuilder

class AppClient(appendable: Appendable) : App(appendable), ClientElement {
  override val helper: Helper = HelperClient()

  override fun renderImplementation(){
    appendBody(
      """
      import { observer } from "mobx-react";
      import * as React from "react";
      import { Notifications } from "../tpl/Notifications";
      import { CarList } from "./CarListTpl";
      import { uiStore } from "../../stores/UIStore";
      import { Modal } from "../tpl/ModalTpl";
      import { CarEditor } from "./CarEditorTpl";
      import { stores } from "../../stores/Stores";
      import { navbar } from "../app/controllers/NavbarController";
      import { app } from "../app/controllers/AppController";

      export const App = observer((props: app.Props) => (""".trimIndent()
    )
    render(this)
    appendBody("));")
  }

  override fun button(className: String, tabindex: Int, onClick: String, block: TagElement.() -> Unit) {
    val button = TagElement(appendable, "button")
    button.appendStart(
      StringBuilder()
        .append(helper.classNameAttr(className))
        .append(helper.onClickAttr(onClick))
        .append(helper.tabIndexAttr(tabindex))
        .toString()
    )
    button.block()
    button.appendEnd()
  }

  override fun modal() {
    TagElement(appendable, "Modal")
      .appendStart()
      .appendEnd()
  }

  override fun navBar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(appendable, "Notifications")
      .appendStart()
      .appendEnd()
  }

  override fun carList() {
    TagElement(appendable, "CarList")
      .appendStart()
      .appendEnd()
  }

  override fun showIfAuthenticated(block: App.() -> Unit) {
    appendBody("{stores.user.authenticated && (")
    block()
    appendBody(")}")
  }

  override fun showIfEditableAndAuthenticated(block: App.() -> Unit) {
    appendBody("{stores.carEdition.isReadyToEdition && stores.user.authenticated && (")
    block()
    appendBody(")}")
  }

  override fun carEditor() {
    TagElement(appendable, "CarEditor")
      .appendStart()
      .appendEnd()
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }
}
