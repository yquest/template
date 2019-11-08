package pt.fabm.tpl.component.app

import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class CarList(appendable: Appendable) : Element(appendable),
  MultiEnvTemplate {

  abstract fun ifHasCars(block: CarList.() -> Unit)
  abstract fun ifHasNoCars(block: CarList.() -> Unit)

  fun render() {
    ifHasNoCars {
      div { +"no cars available" }
    }
    ifHasCars {
      div(className = "form-group") {
        table {
          thead {
            tr {
              th { +"Make" }
              th { +"Model" }
              th { +"Maturity date" }
              th { +"Price" }
              showIfAuthenticated {
                th(colspan = 2) { +"Actions" }
              }
            }
          }
          tbody {
            carLines()
          }
        }
      }
    }
  }

  protected abstract fun colspanTr(colspan: Int): String

  fun div(className: String? = null, block: CarList.() -> Unit) {
    val div = TagElement(appendable, "div").startStarterTag()
    //attributes
    if (className != null) appendClassName(className)
    div.endStarterTag()
    block()
    div.endTag()
  }

  private fun appendElement(name: String, block: CarList.() -> Unit) {
    TagElement(appendable, name)
      .noAttributesStarterTag()
      .doContainedBlock(this, block)
      .endTag()
  }

  fun table(block: CarList.() -> Unit) = appendElement(name = "table", block = block)
  fun tr(block: CarList.() -> Unit) = appendElement(name = "tr", block = block)
  fun thead(block: CarList.() -> Unit) = appendElement(name = "thead", block = block)
  fun tbody(block: CarList.() -> Unit) = appendElement(name = "tbody", block = block)
  fun th(colspan: Int? = null, block: CarList.() -> Unit) {
    val th = TagElement(appendable, "th")
      .startStarterTag()
    //attributes
    if(colspan != null) appendable.append(colspanTr(colspan))

    th.endStarterTag()
    block()
    th.endTag()
  }

  abstract fun carLines()
  abstract fun showIfAuthenticated(block: CarList.() -> Unit)

}
