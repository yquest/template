package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.Element
import pt.fabm.tpl.component.MultiEnvTemplate
import pt.fabm.tpl.component.TagElement

abstract class CarList(buffer: Buffer) : Element(buffer),
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
    val div = TagElement(buffer, "div").startStarterTag()
    //attributes
    if (className != null) appendClassName(className)
    div.endStarterTag()
    block()
    div.endTag()
  }

  private fun appendElement(name: String, block: CarList.() -> Unit) {
    TagElement(buffer, name)
      .noAttributesStarterTag()
      .doContainedBlock(this, block)
      .endTag()
  }

  fun table(block: CarList.() -> Unit) = appendElement(name = "table", block = block)
  fun tr(block: CarList.() -> Unit) = appendElement(name = "tr", block = block)
  fun thead(block: CarList.() -> Unit) = appendElement(name = "thead", block = block)
  fun tbody(block: CarList.() -> Unit) = appendElement(name = "tbody", block = block)
  fun th(colspan: Int? = null, block: CarList.() -> Unit) {
    val th = TagElement(buffer, "th")
      .startStarterTag()
    //attributes
    if(colspan != null) buffer.appendString(colspanTr(colspan))

    th.endStarterTag()
    block()
    th.endTag()
  }

  abstract fun carLines()
  abstract fun showIfAuthenticated(block: CarList.() -> Unit)

}
