package pt.fabm.tpl.test

import java.lang.StringBuilder

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
  abstract val helper:Helper
  abstract fun start()
  abstract fun end()
  abstract fun showIfAuthenticated(block: CarView.() -> Unit)
  abstract fun showIfBlockedRemove(block: CarView.() -> Unit)
  abstract fun showIfBlockedNotRemove(block: CarView.() -> Unit)
  fun i(className: String){
    TagElement(appendable,"i")
      .appendStart(helper.classNameAttr(className))
      .appendEnd()
  }
  fun a(className: String, onClick: String? = null, block: CarView.() -> Unit){
    val a=TagElement(appendable, "a").appendStart(
      StringBuilder()
        .append(helper.classNameAttr(className))
        .append(helper.onClickEval(onClick))
        .toString()
    )
    this.block()
    a.appendEnd()
  }
  fun td(block: CarView.() -> Unit){
    val td = TagElement(appendable,"td").appendStart()
    this.block()
    td.appendEnd()
  }
}
