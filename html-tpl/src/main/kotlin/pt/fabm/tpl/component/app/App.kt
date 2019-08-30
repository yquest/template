package pt.fabm.tpl.component.app

import pt.fabm.template.models.Car
import pt.fabm.tpl.*
import pt.fabm.tpl.component.car.CarList

class App(
  override val type: Type,
  var auth: Boolean,
  var carEdit: Boolean,
  var carList: List<Car>,
  var username: ()->String
) : ElementCreator {

  override fun create(): Element {
    val appStateNoAuth = { !auth } to "props.appState === app.AppState.LIST_NO_AUTH"
    val carEditAuth = { carEdit } to "props.appState === app.AppState.CAR_EDIT_AUTH"
    val listAuthAndCarEdit = { !auth || carEdit } to "(props.appState === app.AppState.LIST_NO_AUTH || " +
      "props.appState === app.AppState.CAR_EDIT_AUTH)"
    val username: Pair<() -> String, String> = { "Hello ${username()} " } to "Hello {props.username + \" \"}"

    fun createContainerApp(type: Type, init: DIV.() -> Unit): DIV {
      val div = DIV(type) {
        AttributeValue.render(type,AttributeValue.create {
          className("container app")
        })
      }
      div.init()
      return div
    }

    val containerApp = createContainerApp(type) {
      fun showIf(clause: Pair<() -> Boolean, String>, init: ShowIf.() -> Unit): ShowIf {
        val showIf = ShowIf(clause, type)
        showIf.init()
        this.children += showIf
        return showIf
      }
      showIf(appStateNoAuth) {
        a(href = "javascript:void();", onClick = "props.login") {
          +"Sign in"
          i(className = "fas fa-sign-in-alt")
        }
      }
      children += Notifications(type.toFirstLevel())
      showIf(carEditAuth) {
        div(className = "float-right", key = "helloUsername") {
          +username
          a(href = "javascript:void(0)", onClick = "props.loginOff") {
            +"logoff"
            i(className = "fas fa-sign-out-alt")
          }
        }
      }
      showIf(listAuthAndCarEdit) {
        component(CarList(type = type, carEdit = carEdit, list = carList))
      }
    }

    return if (type == Type.SERVER)
      html(type) {
        head {
          link(href = "favicon", rel = "shortcut icon")
          link(href = "main.css", rel = "stylesheet")
        }
        body {
          div(id = "root") {
            children.add(containerApp)
          }
        }
      }.create()
    else
      ElementWrapper(
        prefix = """
        import { observer } from "mobx-react";
        import { app } from "../app/props/AppProps";
        import * as React from "react";
        import { Notifications } from "../app/Notifications";
        import { CarList } from "./CarListTpl";
        
        export const App = observer((props: app.Props) => ( 
        """.trimIndent(),
        children = listOf(containerApp.create()),
        suffix = """
        ));
        """.trimIndent()
      )
  }
}
