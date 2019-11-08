package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient
import pt.fabm.tpl.component.TagElement

class RegisterUserClient(appendable: Appendable) : RegisterUser(appendable), ClientElement,
  MultiEnvTemplateClient {

  override fun modal() {
    TagElement(appendable,"Modal").startStarterTag().endTag()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(appendable,"Notifications").startStarterTag().endTag()
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
    appendable.append("""
    import { observer } from "mobx-react";
    import * as React from "react";
    import { Notifications } from "../tpl/Notifications";
    import { uiStore } from "../../stores/UIStore";
    import { Modal } from "../tpl/ModalTpl";
    import { AppInput } from "./AppInputTpl";
    import { registerPage } from "../app/controllers/RegisterUserController";
    import { navbar } from "../app/controllers/NavbarController";
    
    export const Register = observer((props: registerPage.Props) => ( 
    """.trimIndent())
    render()
    appendable.append("));")
  }


}
