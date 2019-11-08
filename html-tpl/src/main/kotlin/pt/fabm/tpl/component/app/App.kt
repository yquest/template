package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class App(appendable: Appendable) : Element(appendable), MultiEnvTemplate {

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

  abstract fun modal()
  abstract fun navBar()
  abstract fun notifications()
  abstract fun carList()
  abstract fun showIfAuthenticated(block: App.() -> Unit)
  abstract fun showIfEditableAndAuthenticated(block: App.() -> Unit)
  abstract fun carEditor()
  abstract fun modalBackground()
  fun div(className: String, block: App.() -> Unit) {
    val div = TagElement(appendable,"div")
    div.startStarterTag()
    //attributes
    appendClassName(className)
    div.endStarterTag()

    block()
    div.endTag()
  }

  fun button(className: String, tabindex: Int, onClick: String, block: App.() -> Unit){
    val button = TagElement(appendable, "button")
    button.startStarterTag()
    //attributes
    appendClassName(className)
    appendTabIndex(tabindex)
    appendClient(""" onClick={$onClick}""")
    button.endStarterTag()
    this.block()
    button.endTag()
  }
}
