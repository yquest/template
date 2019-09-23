package pt.fabm.tpl.gen

import pt.fabm.tpl.ShowIf
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.page.BasePage
import pt.fabm.tpl.component.page.PageBlocks


fun main() {
  val type = Type.CLIENT_IMPLEMENTATION

  val block: (String) -> ShowIf = { id: String ->
    ShowIf("true", type).apply {
      div { +("content block:" + id) }
    }
  }



  val pb = object : PageBlocks {
    override val loginButtonArea: ShowIf = block("1")
    override val logoffArea: ShowIf = block("2")
    override val carListArea: ShowIf = block("3")
    override val registerArea: ShowIf = block("4")
  }
  BasePage(pb, type).create().renderTag(System.out)
  /*
  TsGen.generateFiles(
    listOf(
      AppGen(),
      CarListGen(),
      CarViewGen(),
      Page2Gen(),
      AppInputGen()
    )
  )*/
}
