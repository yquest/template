package pt.fabm.tpl.test

class RegisterUserClient(appendable: Appendable) : RegisterUser(appendable,true),ClientElement {
  override val attributesBuilder = AttributesBuilderClient()

  override fun modal() {
    TagElement(appendable,isClient,"Modal").startStarterTag().endTag()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun notifications() {
    TagElement(appendable,isClient,"Notifications").startStarterTag().endTag()
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    when (label) {
      Fields.USERNAME -> appendBody("{React.createElement(AppInput, { ...props.username })}")
      Fields.PASSWORD -> appendBody("{React.createElement(AppInput, { ...props.password })}")
      Fields.EMAIL -> appendBody("{React.createElement(AppInput, { ...props.email })}")
    }
  }

  override fun form(onSubmit: String, block: RegisterUser.() -> Unit) {
    val form = TagElement(appendable,isClient,"form")
    form.startStarterTag(" onSubmit={props.submitForm}")
    block()
    form.endTag()
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
