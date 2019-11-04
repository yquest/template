package pt.fabm.tpl.test

class AppClient(appendable: Appendable) : App(appendable), ClientElement {

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
    render()
    appendBody("));")
  }

  override fun modal() {
    TagElement(appendable, "Modal")
      .createSingleTag()
  }

  override fun navBar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(appendable, "Notifications")
      .createSingleTag()
  }

  override fun carList() {
    TagElement(appendable, "CarList")
      .createSingleTag()
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
      .createSingleTag()
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

  override fun appendClient(text: String) {
    appendable.append(text)
  }

  override fun appendServer(text: String) {
    //ignore on server
  }
}
