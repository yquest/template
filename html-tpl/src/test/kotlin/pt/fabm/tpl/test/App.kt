package pt.fabm.tpl.test

import java.lang.Appendable
import java.lang.StringBuilder

abstract class App(appendable: Appendable): Element(appendable) {
  companion object{
    internal fun render(app:App){
      fun app(className: String, block: App.() -> Unit) {
        app.root.appendStart(app.helper.classNameAttr(className))
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
  abstract val helper:Helper
  abstract fun modal()
  abstract fun navBar()
  abstract fun notifications()
  abstract fun carList()
  abstract fun showIfAuthenticated(block:App.()->Unit)
  abstract fun showIfEditableAndAuthenticated(block:App.()->Unit)
  abstract fun carEditor()
  abstract fun modalBackground()
  fun button(className: String, tabindex: Int,onClick:String, block:App.()->Unit){
    val button = TagElement(appendable,"button").appendStart(
      StringBuilder()
        .append(helper.classNameAttr(className))
        .append(helper.tabIndexAttr(tabindex))
        .append(helper.onClickAttr(onClick))
        .toString()
    )
    this.block()
    button.appendEnd()
  }
}
