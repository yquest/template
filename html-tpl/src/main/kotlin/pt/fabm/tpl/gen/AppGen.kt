package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import pt.fabm.tpl.ElementWrapper
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.page.App
import java.io.FileWriter

class AppGen : TsGen {
  override val appendable: FileWriter = FileWriter("AppTpl.tsx")
  override val creator: Element =       ElementWrapper(
    prefix = """
        import { observer } from "mobx-react";
        import { app } from "../app/props/AppProps";
        import * as React from "react";
        import { Notifications } from "../app/Notifications";
        import { CarList } from "./CarListTpl";
        import { uiStore } from "../../UIStore";
        import { Modal } from "../tpl/ModalTpl";
        import { Navbar } from "../gen/NavbarTpl";
        
        export const App = observer((props: app.Props) => ( 
        """.trimIndent(),
    children = listOf(App(Type.CLIENT_IMPLEMENTATION).create()),
    suffix = """
        ));
        """.trimIndent()
  )

  override fun close() {
    appendable.close()
  }
}
