package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.AppInput
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.MultiEnvTemplateClient
import pt.fabm.tpl.component.TagElement

class LoginClient(buffer: Buffer) : Login(buffer), ClientElement,
  MultiEnvTemplateClient {
  private val fragments = Fragments(buffer)
  override fun literalClassName(value: String): String = " className={$value}"

  override fun asClientText(text: String): String? = text
  override fun modal() {
    TagElement(buffer, "Modal").startStarterTag().endStarterTag().endTag()
  }

  override fun navbar() {
    appendBody("{navbar.createComponent()}")
  }

  override fun renderImplementation() {
    appendBody(
      """
    import { observer } from "mobx-react";
    import { loginPage } from "../app/controllers/LoginController";
    import * as React from "react";
    import { Notifications } from "../tpl/Notifications";
    import { uiStore } from "../../stores/UIStore";
    import { Modal } from "../tpl/ModalTpl";
    import { AppInput } from "./global/AppInputTpl";
    import { navbar } from "../app/controllers/NavbarController";

    export const Login = observer((props: loginPage.Props) => (
    """.trimIndent()
    )
    render()
    appendBody("));")
  }

  override fun notifications() {
    fragments.clientNotifications()
  }

  override fun appInput(label: String, tabIndex: Int, type: AppInput.Type) {
    when (label) {
      LOGIN -> appendBody("{React.createElement(AppInput, { ...props.login })}")
      PASSWORD -> appendBody("{React.createElement(AppInput, { ...props.password })}")
    }
  }

  override fun clientText(text: String) {
    appendBody(text)
  }

  override fun showIfErrorForm(block: Login.() -> Unit) {
    appendBody("{props.showErrorForm && (")
    block()
    appendBody(")}")
  }

  override fun modalBackground() {
    appendBody("""{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""")
  }

  override fun appendClient(text: String) {
    appendBody(text)
  }

  override fun appendServer(text: String) {
    //ignore
  }

}
