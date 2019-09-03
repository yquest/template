package pt.fabm.tpl.component.car

import pt.fabm.template.models.Car
import pt.fabm.tpl.*

class CarList(type: Type, var carEdit: Boolean = false, var list: List<Car> = emptyList()) :
  Component("CarList", type) {
  override val attributes = {
    AttributeValue.render(
      type,
      AttributeValue.create { clientAttribute("cars", "props.cars") },
      AttributeValue.create { clientAttribute("authenticated", "props.authenticated") },
      AttributeValue.create { clientAttribute("carManagerCreator", "props.carManagerCreator") }
    )
  }

  init {
    fun initContent(init: Table.() -> Unit): Element {
      fun initTable(): Table {
        val table = Table(type)
        table.init()
        return table
      }

      if (type == Type.SERVER) {
        if (list.isNotEmpty()) {
          return initTable().create()
        }
        error("not expected")
      } else if (type == Type.CLIENT_IMPLEMENTATION) {
        return ElementWrapper(
          prefix = """const content = (props: carList.Props) => (""",
          children = listOf(initTable().create()),
          suffix = """);"""
        )
      }
      error("not expected")
    }

    fun initEmpty(init: DIV.() -> Unit): Element {
      fun initDiv(): DIV {
        val div = DIV(type) { "" }
        div.init()
        return div
      }
      if (type == Type.SERVER) {
        if (list.isEmpty()) {
          return initDiv().create()
        }
        error("not expected")
      } else if (type == Type.CLIENT_IMPLEMENTATION) {
        return ElementWrapper(
          prefix = """const noContent = () => (""",
          children = listOf(initDiv().create()),
          suffix = """);"""
        )
      }
      error("not expected")
    }


    val emptyElementCreator = {
      initEmpty {
        +"no cars available"
      }
    }

    val contentElementCreator = {
      initContent {
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
          children += eachElement.toElementCreator(type.toFirstLevel())
        }
      }
    }

    if (type == Type.CLIENT_IMPLEMENTATION) {
      children += emptyElementCreator().toElementCreator(type)
      children += contentElementCreator().toElementCreator(type)
      children += TextElement(
        """
        export const CarList = (props: carList.Props) => 
          props.cars.length === 0 ? noContent() : content(props);
        """.trimIndent()
      ).toElementCreator(type)
    } else if (type == Type.SERVER) {
      children += object : Element {
        override val children: Iterable<Element>
          get() {
            return if (list.isEmpty()) listOf(emptyElementCreator())
            else listOf(contentElementCreator())
          }
      }.toElementCreator(type)
    }
  }
}
