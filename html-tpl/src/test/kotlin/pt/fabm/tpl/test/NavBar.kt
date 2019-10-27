package pt.fabm.tpl.test

abstract class NavBar(appendable: Appendable) : Element(appendable) {
  companion object{
    fun render(navBarCreator: ()->NavBar){
      fun navBar(className: String, block: NavBar.() -> Unit) {
        val navBar = navBarCreator()
        navBar.start(className)
        navBar.block()
        navBar.end()
      }

      navBar("d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom") {
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
  }

  abstract fun start(className: String)
  abstract fun end()
  abstract fun i(className: String)
  abstract fun h5(className: String, onClick: String, block: TagElement.() -> Unit)
  abstract fun showIfAuthenticated(block: NavBar.() -> Unit)
  abstract fun showIfNotAuthenticated(block: NavBar.() -> Unit)
  abstract fun a(className: String? = null, onClick: String, block: TagElement.() -> Unit)
}

