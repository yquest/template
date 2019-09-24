package pt.fabm.tpl.component.app

import pt.fabm.tpl.*

class NavBar(
  type: Type,
  val showCarEditArea: () -> Boolean,
  val showLoginButtonArea: () -> Boolean
) : Component("Navbar", type, {
  AttributeValue.render(type,
    AttributeValue.create { clientAttribute("appState", "props.appState") },
    AttributeValue.create { clientAttribute("loginOff", "props.loginOff") },
    AttributeValue.create { clientAttribute("loginOn", "props.loginOn") }
  )
}) {
  override fun create(): Element {
    val root = DIV(type) {
      AttributeValue.render(type,
        AttributeValue.create { className("container app") }
      )
    }
    root.apply {
      div(className = "d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom") {
        header(headerType = 5, className = "my-0 mr-md-auto font-weight-normal") {
          +"Company name"
          nav("my-2 my-md-0 mr-md-3") {
            a(className = "p-2 text-dark") { +"Features" }
            a(className = "p-2 text-dark") { +"Enteprise" }
            a(className = "p-2 text-dark") { +"Suport" }
          }
          children += ShowIf(showCarEditArea to "props.appState === app.AppState.CAR_EDIT_AUTH", type).apply {
            a(className = "btn btn-outline-primary", onClick = "props.loginOff") {
              +"Sign off "
              i(className = "fas fa-sign-out-alt")
            }
          }
          children += ShowIf(showLoginButtonArea to "props.appState === app.AppState.LIST_NO_AUTH", type).apply {
            a(href = "", onClick = "props.loginOn") {
              +"Sign in "
              i(className = "fas fa-sign-in-alt")
            }
          }
        }
      }
    }

    val rootElement = root.create()

    return if (type == Type.CLIENT_IMPLEMENTATION) {
      ElementWrapper(
        prefix = """
        import { app } from "../app/props/AppProps";
        import * as React from "react";
        import { navbar } from "../app/props/NavbarProps"

        export const Navbar = (props: navbar.Props) => (
        
        """.trimIndent(),
        children = listOf(rootElement),
        suffix = ");"
      )
    } else {
      rootElement
    }

  }
}
