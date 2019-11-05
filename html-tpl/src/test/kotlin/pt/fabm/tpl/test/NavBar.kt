package pt.fabm.tpl.test

abstract class NavBar(appendable: Appendable) : Element(appendable), MultiEnvTemplate {

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
    val i = TagElement(appendable, "i")
    i.startStarterTag()
    //attributes
    appendClassName(className)
    i.endStarterTag()
    i.endTag()
  }

  fun h5(className: String, onClick: String, block: NavBar.() -> Unit) {
    val h5 = TagElement(appendable, "h5")
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
    val div = TagElement(appendable, "div")
      .startStarterTag()
    appendClassName(className)
    this.block()
    div.endTag()
  }

  fun a(className: String? = null, onClick: String, block: NavBar.() -> Unit) {

    val a = TagElement(appendable, "a")
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

