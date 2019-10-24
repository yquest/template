package pt.fabm.tpl.component.car

import pt.fabm.template.models.Car
import pt.fabm.tpl.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class CarView(car: () -> Car = { error("only for Server use") }, edit: Boolean = false, type: Type) : Component("CarView", type) {
  override val attributes = { "" }

  init {

    fun formattedDate(date: Instant): String {
      val strFormat = "yyyy-MM-dd, HH:mm"
      val formatter = SimpleDateFormat(strFormat)
      return formatter.format(Date(date.toEpochMilli()))
    }

    fun initDiv(init: CarView.() -> Unit) {
      this.init()
    }

    fun tr(init: TR.() -> Unit): TR {
      val tr = TR(type)
      tr.init()
      children += tr
      return tr
    }

    fun TR.showIfAuthenticated(block:TR.()->Unit){
      conditionElement(this,{edit},"props.authenticated", block)
    }

    initDiv {
      tr {
        td {
          +({ car().make.name } to "{props.maker}")
        }
        td {
          +({ car().model } to "{props.model}")
        }
        td {
          +({ formattedDate(car().maturityDate) } to "{props.maturityDate}")
        }
        td {
          +({ car().price.toString() + "€" } to "{props.price}")
        }
        showIfAuthenticated {
          td {
            a(className = "btn", onClick = "props.carManager.edit") {
              i(className = "fas fa-edit")
            }
          }
        }
        showIfAuthenticated {
          td {
            a(className = "btn", onClick = "props.carManager.remove"){
              i(className = "fas fa-times")
            }
          }
        }
      }
    }



  }
}
