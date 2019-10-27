package pt.fabm.tpl.test

import java.lang.Appendable

abstract class App(appendable: Appendable): Element(appendable) {
  companion object{
    fun render(appCreator:()->App){
      fun app(className: String, block: App.() -> Unit) {
        val appClient = appCreator()
        appClient.start(className)
        appClient.block()
        appClient.end()
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
  }

  abstract fun start(className: String)
  abstract fun end()
  abstract fun modal()
  abstract fun navBar()
  abstract fun notifications()
  abstract fun carList()
  abstract fun showIfAuthenticated(block:App.()->Unit)
  abstract fun showIfEditableAndAuthenticated(block:App.()->Unit)
  abstract fun carEditor()
  abstract fun modalBackground()
  abstract fun button(className: String, tabindex: Int,onClick:String, block:TagElement.()->Unit)
}
