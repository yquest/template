package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class CarView(appendable: Appendable) : Element(appendable), MultiEnvTemplate {

  protected fun render(cars: List<CarFields>) {
    cars.forEach { carFields ->
      tr(classClientVar = "props.classes") {
        td { +carFields.maker }
        td { +carFields.model }
        td { +carFields.matDate }
        td { +carFields.price }
        showIfAuthenticated {
          td {
            a(className = "btn", onClick = "props.onEdit") {
              i(className = "fas fa-edit")
            }
          }
        }
        showIfAuthenticated {
          td {
            showIfBlockedRemove {
              i(className = "btn fas fa-times text-muted")
            }
            showIfBlockedNotRemove {
              a(className = "btn", onClick = "props.onRemove") {
                i(className = "fas fa-times")
              }
            }
          }
        }
      }
    }
  }

  abstract fun showIfAuthenticated(block: CarView.() -> Unit)
  abstract fun showIfBlockedRemove(block: CarView.() -> Unit)
  abstract fun showIfBlockedNotRemove(block: CarView.() -> Unit)

  fun tr(classClientVar: String, block: CarView.() -> Unit) {
    val tr = TagElement(appendable, "tr")
      .startStarterTag()
    appendClient(" className={$classClientVar}")
    tr.endStarterTag()
    this.block()
    tr.endTag()
  }

  fun i(className: String) {
    val i = TagElement(appendable, "i")
      .startStarterTag()
    //attributes
    appendClassName(className)
    i.endStarterTag()
    i.endTag()
  }

  fun a(className: String, onClick: String, block: CarView.() -> Unit) {
    val a = TagElement(appendable,  "a")
      .startStarterTag()
    //attributes
    emptyHref()
    appendClassName(className)
    appendClient(" onClick={$onClick}")

    a.endStarterTag()
    this.block()
    a.endTag()
  }

  fun td(block: CarView.() -> Unit) {
    val td = TagElement(appendable,  "td").noAttributesStarterTag()
    this.block()
    td.endTag()
  }
}
