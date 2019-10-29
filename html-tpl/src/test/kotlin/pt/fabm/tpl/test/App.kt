package pt.fabm.tpl.test

import java.lang.Appendable
import java.lang.StringBuilder

abstract class App(appendable: Appendable): Element(appendable) {
  companion object{
    internal fun render(app:App){
      fun app(className: String, block: App.() -> Unit) {
        app.attributesBuilder.builder.clear()
        app.attributesBuilder.classNameAttr(className)
        app.root.appendStart(app.attributesBuilder.builder.toString())
        app.block()
        app.root.appendEnd()
      }

      app(className = "container app") {
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
    fun render(appCreator:()->App){
      render(appCreator())
    }
  }

  internal val root = TagElement(appendable,"div")
  abstract val attributesBuilder:AttributesBuilder
  abstract fun modal()
  abstract fun navBar()
  abstract fun notifications()
  abstract fun carList()
  abstract fun showIfAuthenticated(block:App.()->Unit)
  abstract fun showIfEditableAndAuthenticated(block:App.()->Unit)
  abstract fun carEditor()
  abstract fun modalBackground()
  fun button(className: String, tabindex: Int,onClick:String, block:App.()->Unit){
    attributesBuilder.builder.clear()
    attributesBuilder
      .classNameAttr(className)
      .tabIndexAttr(tabindex)
      .onClickAttr(onClick)

    val button = TagElement(appendable,"button")
      .appendStart(attributesBuilder.builder.toString())
    this.block()
    button.appendEnd()
  }
}
