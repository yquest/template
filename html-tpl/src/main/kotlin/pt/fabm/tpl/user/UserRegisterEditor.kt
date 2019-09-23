package pt.fabm.tpl.user

import pt.fabm.tpl.AttributeValue
import pt.fabm.tpl.Component
import pt.fabm.tpl.Type


class UserRegisterEditor(type: Type) : Component(
  name = "UserRegisterEditor",
  type = type,
  attributes = { AttributeValue.render(type,
    AttributeValue.create { clientAttribute("returnToLoginClick","props.something") },
    AttributeValue.create { clientAttribute("successfullyRegistered","props.somethingElse") }
  ) }
) {



}
