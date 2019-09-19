package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.app.Modal
import java.io.FileWriter

class ModalGen:TsGen{
  override val appendable: FileWriter = FileWriter("ModalTpl.tsx")
  override val creator: Element = Modal(Type.CLIENT_IMPLEMENTATION).create()
  override fun close() {
    appendable.close()
  }
}
