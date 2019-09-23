package pt.fabm.tpl.component.page

import pt.fabm.tpl.*

class BasePage(
  val pageBlocks: PageBlocks,
  override val type: Type
) : ElementCreator {

  override fun create(): Element {
    val rootAttributes = {
      AttributeValue.render(
        type,
        AttributeValue.create { className("container app") }
      )
    }
    val root = DIV(type, rootAttributes)
    root.children += pageBlocks.loginButtonArea
    //to be rendered only on client
    root.children += Component("Notifications", type)
    root.children += pageBlocks.logoffArea
    root.children += pageBlocks.carListArea
    root.children += pageBlocks.registerArea
    return root.create()
  }

}
