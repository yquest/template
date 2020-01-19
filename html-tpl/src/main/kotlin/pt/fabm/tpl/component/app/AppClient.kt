package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient
import pt.fabm.tpl.component.TagElement

class AppClient(buffer: Buffer) : App(buffer), ClientElement, MultiEnvTemplateClient {
  private val fragments = Fragments(buffer)
  override fun renderImplementation() {
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
    render()
    appendBody("));")
  }

  override fun modal() {
    TagElement(buffer, "Modal").createSingleTag()
  }

  override fun navBar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    fragments.clientNotifications()
  }

  override fun carList() {
    TagElement(buffer, "CarList").createSingleTag()
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
    TagElement(buffer, "CarEditor")
      .createSingleTag()
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

}
