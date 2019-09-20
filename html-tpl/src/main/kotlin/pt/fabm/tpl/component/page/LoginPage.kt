package pt.fabm.tpl.component.page

import pt.fabm.tpl.Element
import pt.fabm.tpl.ElementCreator
import pt.fabm.tpl.ShowIf
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.page.PageBlocks.*

class LoginPage(override val type: Type) : ElementCreator {
  val logoutArea: ShowIf
    get() {
      val area = ShowIf({ false } to "", type)
      area.a(className = "float-right", onClick = "props.loginOn") {
        +"Sign in"
        i(className = "fas fa-sign-in-alt")
      }
      return area
    }

  val loginArea: ShowIf
    get() {
      //ignore in server
      val area = ShowIf({ false } to ClientText.showLoginButton, type)
      area.a(className = "float-right", onClick = "props.loginOn") {
        +"Sign in"
        i(className = "fas fa-sign-in-alt")
      }
      return area
    }
  val carListArea: ShowIf get() = ShowIf({ false } to ClientText.showCarlistArea, type)
  val registerArea: ShowIf get() = TODO("not implemented")
  override fun create(): Element {
    val loginPage = object : PageBlocks {
      override val logoutArea: ShowIf
        get() {
          val area = ShowIf({ false } to text.showLoginButton, type)
          area.a(className = "float-right", onClick = "props.loginOn") {
            +"Sign in"
            i(className = "fas fa-sign-in-alt")
          }
          return area
        }
      override val loginArea: ShowIf
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
      override val carListArea: ShowIf
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
      override val registerArea: ShowIf
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }
  }
}
