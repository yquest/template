package pt.fabm.tpl.test

abstract class NavBar(appendable: Appendable) : Element(appendable) {

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

  abstract val attributesBuilder: AttributesBuilder
  fun i(className: String) {
    TagElement(appendable, "i")
      .appendStart(
        attributesBuilder
          .classNameAttr(className)
          .build()
      )
      .appendEnd()
  }

  fun h5(className: String, onClick: String, block: NavBar.() -> Unit) {

    val h5 = TagElement(appendable, "h5")
      .appendStart(
        attributesBuilder
          .classNameAttr(className)
          .onClickAttr(onClick)
          .build()
      )
    this.block()
    h5.appendEnd()
  }

  abstract fun showIfAuthenticated(block: NavBar.() -> Unit)

  abstract fun showIfNotAuthenticated(block: NavBar.() -> Unit)
  fun div(className: String, block: NavBar.() -> Unit) {
    val div = TagElement(appendable, "div")
      .appendStart(
        attributesBuilder
          .classNameAttr(className)
          .build()
      )
    this.block()
    div.appendEnd()
  }

  fun a(className: String? = null, onClick: String, block: NavBar.() -> Unit) {

    val a = TagElement(appendable, "a")
      .appendStart(    attributesBuilder
        .emptyHref()
        .classNameEval(className)
        .onClickAttr(onClick)
        .build()
      )
    this.block()
    a.appendEnd()
  }
}

