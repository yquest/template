package pt.fabm.tpl.component.car

import pt.fabm.template.models.Car
import pt.fabm.tpl.*

class CarList(type: Type, var carEdit: Boolean = false, var list: List<Car> = emptyList()) : Component("CarList", type) {
  override val attributes = {
    AttributeValue.render(
      type,
      AttributeValue.create { clientAttribute("cars", "props.cars") },
      AttributeValue.create { clientAttribute("authenticated", "props.authenticated") },
      AttributeValue.create { clientAttribute("carManagerCreator", "props.carManagerCreator") }
    )
  }

  init {

    fun initDiv(init: CarList.() -> Unit) {
      this.init()
    }

    initDiv {
      table {
        thead {
          tr {
            th { +"Make" }
            th { +"Model" }
            th { +"Maturity date" }
            th { +"Price" }
            showIf({ true } to "props.authenticated") {
              th(colSpan = 2) {
                +"Actions"
              }
            }
          }
        }
        tbody {
          val eachElement = if (type == Type.SERVER) {

            NoTagElement(Iterable {
              val listElements =
                listOf(CommentServerTag("start each CarView", type).create()) +
                  list.map { CarView({ it }, carEdit, Type.SERVER).create() } +
                  listOf(CommentServerTag("end each CarView", type).create())
              listElements.iterator()
            })
          } else {
            ElementWrapper(
              prefix = "{props.cars.map((car, idx) => (",
              children = listOf(
                CarView({ error("not used") }, carEdit, type.toFirstLevel()).create()
              ),
              suffix = "))}"
            )
          }
          children += object : ElementCreator {
            override val type: Type get() = type.toFirstLevel()
            override fun create(): Element = eachElement
          }
        }
      }
    }
  }
}
