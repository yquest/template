package pt.fabm.tpl.component.car

import pt.fabm.tpl.AttributeValue
import pt.fabm.tpl.Component
import pt.fabm.tpl.DIV
import pt.fabm.tpl.Type

class CarEditor(type: Type) : Component(
  type = type,
  name = "CarEditor",
  attributes = {
    AttributeValue.render(type,
      AttributeValue.create { clientAttribute("saveCarEvent", "props.saveCarEvent") }
    )
  }
) {

}
