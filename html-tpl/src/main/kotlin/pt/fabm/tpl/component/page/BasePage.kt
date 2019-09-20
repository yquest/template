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
    root.children += pageBlocks.logoutArea
    root.children += pageBlocks.loginArea
    root.children += pageBlocks.carListArea
    root.children += pageBlocks.registerArea
    return root.create()
  }

  protected fun addLoginButton() {
    fun A.initComponent(init:A.()->Unit):A{
      this.init()
      return this
    }
    val button = A(type,).initComponent {

    }
    area.a(className = "float-right", onClick = "props.loginOn") {
      +"Sign in"
      i(className = "fas fa-sign-in-alt")
    }
    return area
  }
}
