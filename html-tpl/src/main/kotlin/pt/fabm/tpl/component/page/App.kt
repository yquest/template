package pt.fabm.tpl.component.page

import pt.fabm.template.models.Car
import pt.fabm.tpl.*
import pt.fabm.tpl.component.app.NavBar
import pt.fabm.tpl.component.car.CarList

class App(
  override val type: Type,
  val authenticated: () -> Boolean = {false},
  val readyToEdition: () -> Boolean = {false},
  private val carList:List<Car> = emptyList()
) : ElementCreator {

  override fun create(): Element {

    fun DIV.navbar() {
      children += NavBar(type, authenticated())
    }

    fun DIV.showIfAuthenticated(block: DIV.() -> Unit) {
      conditionElement(this,authenticated,"stores.user.authenticated",block)
    }

    fun DIV.showIfAuthenticatedAndReadyToEdition(block: DIV.() -> Unit) {
      val jsCondition = "stores.carEdition.isReadyToEdition && stores.user.authenticated"
      val javaCondition = { authenticated() && readyToEdition() }
      conditionElement(this,javaCondition, jsCondition, block)
    }

    fun DIV.carList() {
      initTag(CarList(
        type = type,
        authenticated = authenticated(),
        carEdit = readyToEdition()
      ))
    }

    fun DIV.button(className: String, init: Button.() -> Unit) {
      initTag(
        Button(
          type = type,
          attributes = {
            AttributeValue.render(type,
              AttributeValue.create { className(className) }
            )
          }
        ), init)
    }

    fun DIV.modalBackground() {
      if (type == Type.CLIENT_IMPLEMENTATION) {
        children += TextVarCreator(
          { error("not expected") },
          """{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""",
          Type.CLIENT_IMPLEMENTATION
        )
      }
    }
    fun DIV.modal() {
      if (type == Type.CLIENT_IMPLEMENTATION) {
        children += TextVarCreator(
          { error("not expected") },
          """{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}""",
          Type.CLIENT_IMPLEMENTATION
        )
      }
    }

    val root = Component("App", type, attributes = { "createCarClick={appProps.createCarClick}" })
      .apply {
        div(className = "container app") {

          navbar()
          carList()
          showIfAuthenticated {
            button(className = "btn btn-primary form-group") {
              +"Create Car"
            }
          }
          showIfAuthenticatedAndReadyToEdition {
            TODO("add car editor")
          }
          modalBackground()
        }
      }
    return root.create()
  }

}
