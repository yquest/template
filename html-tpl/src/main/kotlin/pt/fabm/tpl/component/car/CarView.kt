package pt.fabm.tpl.component.car

import pt.fabm.template.models.Car
import pt.fabm.tpl.*

class CarView(car: () -> Car, edit: Boolean, type: Type) : Component("CarView", type) {
  override val attributes = {
    AttributeValue.render(
      type,
      AttributeValue.create { clientAttribute("authenticated", "props.authenticated") },
      AttributeValue.create { clientAttribute("key", "idx") },
      AttributeValue.create { clientAttribute("maker", "MAKERS[car.make]") },
      AttributeValue.create { clientAttribute("model", "car.model") },
      AttributeValue.create { clientAttribute("maturityDate", "dateToStringReadable(car.maturityDate)") },
      AttributeValue.create { clientAttribute("price", """car.price + "€"""") },
      AttributeValue.create { clientAttribute("carManager", "props.carManagerCreator(car)") },
      AttributeValue.create { clientAttribute("car", "car") }
    )
  }

  init {

    fun initDiv(init: CarView.() -> Unit) {
      this.init()
    }

    fun tr(init: TR.() -> Unit): TR {
      val tr = TR(type)
      tr.init()
      children += tr
      return tr
    }

    fun comment(element: WithChildren, text: String) {
      element.children += CommentServerTag(text, type)
    }

    initDiv {
      tr {
        fun showIf(clause: Pair<() -> Boolean, String>, init: ShowIf.() -> Unit): ShowIf {
          val showIf = ShowIf(clause, type)
          showIf.init()
          this.children += showIf
          return showIf
        }
        td {
          +({ car().make.name } to "{props.maker}")
        }
        td {
          +({ car().model } to "{props.model}")
        }
        td {
          +({ car().maturityDate.toString() } to "{props.maturityDate}")
        }
        td {
          +({ car().price.toString() } to "{props.price}")
        }
        showIf({ edit } to "props.authenticated") {
          comment(this, "only authenticated")
          td {
            a(href = "javascript:void(0)", className = "btn", onClick = "{props.carManager.edit}")
          }
        }
        showIf({ edit } to "props.authenticated") {
          comment(this, "only authenticated")
          td {
            a(href = "javascript:void(0)", className = "btn", onClick = "{props.carManager.remove}")
          }
        }
      }
    }
  }
}
