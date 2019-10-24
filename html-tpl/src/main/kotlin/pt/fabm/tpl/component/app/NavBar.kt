package pt.fabm.tpl.component.app

import pt.fabm.tpl.*

class NavBar(
  override val type: Type,
  private val authenticated: Boolean = false
) : ElementCreator {
  override fun create(): Element {
    val root = DIV(type) {
      AttributeValue.render(type, AttributeValue.create {
        className("d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom")
      })
    }

    fun DIV.showIfAuthenticated(block: DIV.() -> Unit) {
      conditionElement(this, { authenticated }, "stores.user.authenticated", block)
    }

    fun DIV.showIfNotAuthenticated(block: DIV.() -> Unit) {
      conditionElement(this, { !authenticated }, "!stores.user.authenticated", block)
    }

    root.apply {
      header(
        headerType = 5,
        onClick = "stores.navigation.root",
        className = "my-0 mr-md-auto font-weight-normal pointer-cursor"
      ) { +"Company name" }
      showIfAuthenticated {
        a(className = "btn btn-outline-primary", onClick = "props.loginOff") {
          +"Sign off"
          i(className = "fas fa-sign-out-alt")
        }
      }
      showIfNotAuthenticated {
        a(className = "btn btn-outline-primary", onClick = "props.loginOff") {
          +"Sign off"
          i(className = "fas fa-sign-in-alt")
        }
      }
    }
    return root.create()
  }
}
