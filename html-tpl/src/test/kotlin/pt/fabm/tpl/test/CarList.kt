package pt.fabm.tpl.test

abstract class CarList(appendable: Appendable) : Element(appendable) {

  abstract val attributesBuilder: AttributesBuilder

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

  private fun appendElement(attributes: String = "", name: String, block: CarList.() -> Unit) {
    val element = TagElement(appendable, name)
    element.appendStart(attributes)
    this.block()
    element.appendEnd()
  }

  protected abstract fun colspanTr(colspan: Int): String

  fun div(className: String? = null, block: CarList.() -> Unit) =
    appendElement(
      attributes = attributesBuilder
        .clear()
        .classNameEval(className)
        .build(),
      name = "div",
      block = block
    )
  fun table(block: CarList.() -> Unit) = appendElement(name = "table", block = block)
  fun tr(block: CarList.() -> Unit) = appendElement(name = "tr", block = block)
  fun thead(block: CarList.() -> Unit) = appendElement(name = "thead", block = block)
  fun tbody(block: CarList.() -> Unit) = appendElement(name = "tbody", block = block)
  fun th(colspan: Int? = null, block: CarList.() -> Unit) = appendElement(
    name = "th",
    attributes = if (colspan == null) "" else colspanTr(colspan),
    block = block
  )

  abstract fun carLines()
  abstract fun showIfAuthenticated(block: CarList.() -> Unit)

}
