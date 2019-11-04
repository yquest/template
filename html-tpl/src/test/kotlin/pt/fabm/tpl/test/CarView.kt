package pt.fabm.tpl.test

abstract class CarView(appendable: Appendable) : Element(appendable) {
  protected fun render(cars: List<CarFields>) {
    cars.forEach { carFields ->
      tr(classVar = "props.classes") {
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

  abstract val attributesBuilder: AttributesBuilder
  abstract fun showIfAuthenticated(block: CarView.() -> Unit)
  abstract fun showIfBlockedRemove(block: CarView.() -> Unit)
  abstract fun showIfBlockedNotRemove(block: CarView.() -> Unit)

  fun tr(classVar:String,block: CarView.() -> Unit){
    val tr=TagElement(appendable,"tr")
      .appendStart(attributesBuilder.classVarAttr(classVar).build())
    this.block()
    tr.appendEnd()
  }
  fun i(className: String) {
    TagElement(appendable, "i")
      .appendStart(attributesBuilder.classNameAttr(className).build())
      .appendEnd()
  }

  fun a(className: String, onClick: String? = null, block: CarView.() -> Unit) {
    val a = TagElement(appendable, "a")
      .appendStart(attributesBuilder
        .emptyHref()
        .classNameAttr(className)
        .onClickEval(onClick)
        .build()
      )
    this.block()
    a.appendEnd()
  }

  fun td(block: CarView.() -> Unit) {
    val td = TagElement(appendable, "td").appendStart()
    this.block()
    td.appendEnd()
  }
}
