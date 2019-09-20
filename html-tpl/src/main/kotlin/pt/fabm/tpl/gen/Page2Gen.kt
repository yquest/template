package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import pt.fabm.tpl.ElementWrapper
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.page.Page2
import java.io.FileWriter

class Page2Gen : TsGen {
  override val appendable: FileWriter = FileWriter("Page2Tpl.tsx")
  override val creator: Element = ElementWrapper(
    prefix = """
        import { observer } from "mobx-react";
        import { page as app } from "../app/props/PageProps";
        import { app as a } from "../app/props/AppProps";
        import * as React from "react";
        import { Notifications } from "../app/Notifications";
        import { uiStore } from "../../UIStore";
        import { Modal } from "../tpl/ModalTpl"; 
        import { AppInput } from "./AppInputTpl";
        import { form1, submitFormEvent, updateValue, Control } from "../events/Page2Events";
        
        export const Page2 = observer((props: app.Props) => ( 
        """.trimIndent(),
    children = listOf(
      Page2(
        type = Type.CLIENT_IMPLEMENTATION
      ).create()),
    suffix = """
        ));
        """.trimIndent()
  )

  override fun close() {
    appendable.close()
  }

}
