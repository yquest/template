package pt.fabm.tpl.test

abstract class CarView(appendable: Appendable) : Element(appendable) {
  companion object {
    fun render(carViewCreator: () -> CarView, cars: List<CarFields>) {

      fun carView(block: CarView.() -> Unit) {
        val carView = carViewCreator()
        carView.start()
        carView.block()
        carView.end()
      }

      cars.forEach { carFields ->
        carView {
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
  }

  internal val root = TagElement(appendable, "tr")
  abstract val attributesBuilder:AttributesBuilder
  abstract fun start()
  abstract fun end()
  abstract fun showIfAuthenticated(block: CarView.() -> Unit)
  abstract fun showIfBlockedRemove(block: CarView.() -> Unit)
  abstract fun showIfBlockedNotRemove(block: CarView.() -> Unit)
  fun i(className: String){
    attributesBuilder.builder.clear()
    attributesBuilder.classNameAttr(className)
    TagElement(appendable,"i")
      .appendStart(attributesBuilder.builder.toString())
      .appendEnd()
  }
  fun a(className: String, onClick: String? = null, block: CarView.() -> Unit){
    attributesBuilder.builder.clear()
    attributesBuilder
      .classNameAttr(className)
      .onClickEval(onClick)

    val a=TagElement(appendable, "a")
      .appendStart(attributesBuilder.builder.toString())
    this.block()
    a.appendEnd()
  }
  fun td(block: CarView.() -> Unit){
    val td = TagElement(appendable,"td").appendStart()
    this.block()
    td.appendEnd()
  }
}
