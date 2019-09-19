package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import pt.fabm.tpl.ElementWrapper
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.app.App
import pt.fabm.tpl.component.app.AppInput
import java.io.FileWriter

class AppInputGen : TsGen {
  override val appendable: FileWriter = FileWriter("AppInputTpl.tsx")
  override val creator: Element = AppInput(
    type = Type.CLIENT_IMPLEMENTATION).create()

  override fun close() {
    appendable.close()
  }
}
