package pt.fabm.tpl.test

abstract class App(appendable: Appendable) : Element(appendable) {

  fun render() {
    div(className = "container app") {
      modal()
      navBar()
      notifications()
      carList()
      showIfAuthenticated {
        button(className = "btn btn-primary form-group", tabindex = 1, onClick = "props.createCarClick") {
          +"Create car"
        }
      }
      showIfEditableAndAuthenticated {
        carEditor()
      }
      modalBackground()
    }
  }

  private fun appendElement(attributes: String = "", name: String, block: App.() -> Unit) {
    val element = TagElement(appendable, name)
    element.appendStart(attributes)
    this.block()
    element.appendEnd()
  }

  abstract val attributesBuilder: AttributesBuilder
  abstract fun modal()
  abstract fun navBar()
  abstract fun notifications()
  abstract fun carList()
  abstract fun showIfAuthenticated(block: App.() -> Unit)
  abstract fun showIfEditableAndAuthenticated(block: App.() -> Unit)
  abstract fun carEditor()
  abstract fun modalBackground()
  fun div(className: String, block: App.() -> Unit) = appendElement(
    attributes = attributesBuilder
      .classNameAttr(className)
      .build(),
    name = "div",
    block = block
  )

  fun button(className: String, tabindex: Int, onClick: String, block: App.() -> Unit) =
    appendElement(
      attributes = attributesBuilder
        .classNameAttr(className)
        .tabIndexAttr(tabindex)
        .onClickAttr(onClick)
        .build(),
      name = "button",
      block = block
    )
}
