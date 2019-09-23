package pt.fabm.tpl.user

import pt.fabm.tpl.AttributeValue
import pt.fabm.tpl.Component
import pt.fabm.tpl.Type


class LoginEditor(type: Type) : Component(
  name = "LoginEditor",
  type = type,
  attributes = { AttributeValue.render(type,
    AttributeValue.create { clientAttribute("loginSuccessfully","props.something") },
    AttributeValue.create { clientAttribute("showUserRegister","props.somethingElse") }
  ) }
)
