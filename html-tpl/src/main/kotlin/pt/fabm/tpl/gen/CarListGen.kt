package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import pt.fabm.tpl.ElementWrapper
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.car.CarList
import java.io.FileWriter

class CarListGen : TsGen {
  override val appendable: FileWriter = FileWriter("CarListTpl.tsx")
  override val creator: Element = ElementWrapper(
    prefix = """
      import * as React from "react";
      import { carList } from "../app/props/CarListProps";
      import { MAKERS } from "../../model/Car";
      import { CarView } from "./CarViewTpl";
      import { dateToStringReadable } from "../../util";
      
      export const CarList = (props: carList.Props) => (
    """.trimIndent(),
    children = listOf(CarList(Type.CLIENT_IMPLEMENTATION).create()),
    suffix = """
      );    
    """.trimIndent()
  )

  override fun close() {
    appendable.close()
  }
}
