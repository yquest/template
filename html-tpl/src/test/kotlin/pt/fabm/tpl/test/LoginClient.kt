package pt.fabm.tpl.test

class LoginClient(appendable: Appendable) : Login(appendable,true), ClientElement {
  override fun literalClassName(value: String): String = " className={$value}"

  override val attributesBuilder = AttributesBuilderClient()
  override fun asClientText(text: String): String? = text
  override fun modal() {
    TagElement(appendable,true,"Modal").startStarterTag().endTag()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun renderImplementation() {
    appendBody("""
    import { observer } from "mobx-react";
    import { loginPage } from "../app/controllers/LoginController";
    import * as React from "react";
    import { Notifications } from "../tpl/Notifications";
    import { uiStore } from "../../stores/UIStore";
    import { Modal } from "../tpl/ModalTpl";
    import { AppInput } from "./AppInputTpl";
    import { navbar } from "../app/controllers/NavbarController";
    
    export const Login = observer((props: loginPage.Props) => (
    """.trimIndent())
    render()
    appendBody("));")
  }

  override fun notifications() {
    TagElement(appendable,true,"Notifications").startStarterTag().endTag()
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    when (label) {
      Fields.LOGIN -> appendBody("{React.createElement(AppInput, { ...props.login })}")
      Fields.PASSWORD -> appendBody("{React.createElement(AppInput, { ...props.password })}")
    }
  }

  override fun clientText(text: String) {
    appendBody(text)
  }

  override fun form(onSubmit: String, block: Login.() -> Unit) {
    val form = TagElement(appendable,true,"form")
    form.startStarterTag(" onSubmit={props.submitForm}")
    block()
    form.endTag()
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    appendBody("{props.showErrorForm && (")
    block()
    appendBody(")}")
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

}
