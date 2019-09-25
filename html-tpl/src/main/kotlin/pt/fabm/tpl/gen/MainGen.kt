package pt.fabm.tpl.gen

import pt.fabm.tpl.Type
import pt.fabm.tpl.component.app.AppInput
import pt.fabm.tpl.component.app.NavBar

fun main() {
  TsGen.generateFiles(
    listOf(
      AppGen(),
      CarListGen(),
      CarViewGen(),
      Page2Gen(),
      AppInputGen(),
      TsGen.fromElement("NavbarTpl.tsx") {
        NavBar(Type.CLIENT_IMPLEMENTATION, { false }, { false }).create()
      }
    )
  )
}
