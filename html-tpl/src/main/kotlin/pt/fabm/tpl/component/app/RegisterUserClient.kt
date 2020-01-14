package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient
import pt.fabm.tpl.component.TagElement

class RegisterUserClient(buffer: Buffer) : RegisterUser(buffer), ClientElement,
  MultiEnvTemplateClient {

  override fun modal() {
    TagElement(buffer,"Modal").startStarterTag().endStarterTag().endTag()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(buffer,"Notifications").startStarterTag().endStarterTag().endTag()
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    when (label) {
      USERNAME -> appendBody("{React.createElement(AppInput, { ...props.username })}")
      PASSWORD -> appendBody("{React.createElement(AppInput, { ...props.password })}")
      EMAIL -> appendBody("{React.createElement(AppInput, { ...props.email })}")
    }
  }


  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

  override fun renderImplementation() {
    buffer.appendString("""
    import { observer } from "mobx-react";
    import * as React from "react";
    import { Notifications } from "../tpl/Notifications";
    import { uiStore } from "../../stores/UIStore";
    import { Modal } from "../tpl/ModalTpl";
    import { AppInput } from "./global/AppInputTpl";
    import { registerPage } from "../app/controllers/RegisterUserController";
    import { navbar } from "../app/controllers/NavbarController";

    export const Register = observer((props: registerPage.Props) => (
    """.trimIndent())
    render()
    buffer.appendString("));")
  }
}
