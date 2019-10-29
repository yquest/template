package pt.fabm.tpl.test

import java.lang.StringBuilder

abstract class NavBar(appendable: Appendable) : Element(appendable) {
  companion object{
    fun render(navBar:NavBar){
      fun navBar(className: String, block: NavBar.() -> Unit) {
        navBar.root.appendStart(navBar.helper.classNameAttr(className))
        navBar.block()
        navBar.root.appendEnd()
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
    fun render(navBarCreator: ()->NavBar){
      render(navBarCreator())
    }
  }

  abstract val helper:Helper
  abstract val root:TagElement
  fun i(className: String){
    TagElement(appendable,"i")
      .appendStart(helper.classNameAttr(className))
      .appendEnd()
  }
  fun h5(className: String, onClick: String, block: NavBar.() -> Unit){
    val h5 = TagElement(appendable,"h5").appendStart(
      StringBuilder()
        .append(helper.classNameAttr(className))
        .append(helper.onClickAttr(onClick))
        .toString()
    )
    this.block()
    h5.appendEnd()
  }
  abstract fun showIfAuthenticated(block: NavBar.() -> Unit)
  abstract fun showIfNotAuthenticated(block: NavBar.() -> Unit)
  fun a(className: String? = null, onClick: String, block: NavBar.() -> Unit){
    val a = TagElement(appendable,"a").appendStart(
      StringBuilder()
        .append(helper.classNameEval(className))
        .append(helper.onClickAttr(onClick))
        .toString()
    )
    this.block()
    a.appendEnd()
  }
}

