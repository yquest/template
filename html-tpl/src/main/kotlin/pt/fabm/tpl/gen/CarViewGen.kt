package pt.fabm.tpl.gen

import pt.fabm.tpl.*
import pt.fabm.tpl.component.car.CarView
import java.io.FileWriter

class CarViewGen : TsGen {
  override val appendable: FileWriter = FileWriter("CarViewTpl.tsx")
  override val creator: Element = ElementWrapper(
    prefix = """
      import * as React from "react";
      import { carView } from "../app/props/CarViewProps";
      
      export const CarView = (props: carView.Props) => (    
    """.trimIndent(),
    children = listOf(CarView(type = Type.CLIENT_IMPLEMENTATION).create()),
    suffix = """
      );    
    """.trimIndent()
  )
  override fun close() {
    appendable.close()
  }
}
