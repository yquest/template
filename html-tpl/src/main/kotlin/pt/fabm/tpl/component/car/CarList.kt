package pt.fabm.tpl.component.car

import pt.fabm.template.models.Car
import pt.fabm.tpl.*

class CarList(
  override val type: Type,
  val authenticated: Boolean = false,
  var carEdit: Boolean = false,
  var list: List<Car> = emptyList()
) : ElementCreator {
  override fun create(): Element {
    fun TR.showIfAuthenticated(block: TR.() -> Unit) {
      if (authenticated) {
        block()
      }
    }

    fun Tbody.createCarViewList(): ElementCreator {
      if (type == Type.CLIENT) return object : ElementCreator {
        override val type: Type = Type.CLIENT
        override fun create(): Element = TextElement("{carView.carViewList()}")
      } else if (type == Type.SERVER) return object : ElementCreator {
        override val type: Type = Type.SERVER
        override fun create(): Element = NoTagElement(
          list.map {
            CarView({it},authenticated,Type.SERVER).create()
          }
        )
      } else throw IllegalStateException("there is no client implementation expected")
    }

    val root = DIV(type = type, attributes = {
      AttributeValue.render(type,
        AttributeValue.create { className("form-group") }
      )
    })

    root.apply {
      div(className = "form-group") {
        table {
          thead {
            tr {
              th { +"Make" }
              th { +"Model" }
              th { +"Maturity date" }
              th { +"Price" }
              showIfAuthenticated {
                th(colSpan = 2) { +"Actions" }
              }
            }
          }
          tbody {
            createCarViewList()
          }
        }
      }
    }
    return root.create()
  }

}
