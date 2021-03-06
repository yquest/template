package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class NavBar(buffer: Buffer) : Element(buffer), MultiEnvTemplate {

  fun render() {
    div(className = "d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom") {
      h5(className = "my-0 mr-md-auto font-weight-normal pointer-cursor", onClick = "stores.navigation.root") {
        +"Company name"
      }
      showIfAuthenticated {
        a(className = "btn btn-outline-primary", onClick = "props.loginOff") {
          +"Sign off "
          i(className = "fas fa-sign-out-alt")
        }
      }
      showIfNotAuthenticated {
        a(onClick = "props.gotoLoginPage") {
          +"Sign in "
          i(className = "fas fa-sign-in-alt")
        }
      }
    }
  }

  fun i(className: String) {
    val i = TagElement(buffer, "i")
    i.startStarterTag()
    //attributes
    appendClassName(className)
    i.endStarterTag()
    i.endTag()
  }

  fun h5(className: String, onClick: String, block: NavBar.() -> Unit) {
    val h5 = TagElement(buffer, "h5")
    h5.startStarterTag()
    //attributes
    appendClassName(className)
    appendClient(" onClick={$onClick}")
    h5.endStarterTag()
    this.block()
    h5.endTag()
  }

  abstract fun showIfAuthenticated(block: NavBar.() -> Unit)

  abstract fun showIfNotAuthenticated(block: NavBar.() -> Unit)
  fun div(className: String, block: NavBar.() -> Unit) {
    val div = TagElement(buffer, "div")
      .startStarterTag()
    appendClassName(className)
    div.endStarterTag()
    this.block()
    div.endTag()
  }

  fun a(className: String? = null, onClick: String, block: NavBar.() -> Unit) {

    val a = TagElement(buffer, "a")
      .startStarterTag()
    //attributes
    emptyHref()
    if (className != null) appendClassName(className)
    appendClient(" onClick={$onClick}")
    a.endStarterTag()
    this.block()
    a.endTag()
  }
}

