package pt.fabm.tpl.test

abstract class AppInput(appendable: Appendable) : Element(appendable), MultiEnvTemplate {
  enum class Type(val label: String) {
    TEXT("text"),
    PASSWORD("password")
  }

  fun render(
    label: String,
    type: Type,
    value: String,
    tabIndex: String? = null,
    placeHolder: String? = null
  ) {

    div(className = "form-group") {
      label { +label }
      input(
        type = type,
        tabIndex = tabIndex,
        placeHolder = placeHolder,
        value = value
      )
    }
  }


  fun div(className: String, block: AppInput.() -> Unit) {
    val div = TagElement(appendable, "div").startStarterTag()
    //attributes
    appendClassName(className)
    div.endStarterTag()
    this.block()
    div.endTag()
  }

  private fun label(block: AppInput.() -> Unit) {
    val label = TagElement(appendable, "label").noAttributesStarterTag()
    this.block()
    label.endTag()
  }

  internal fun input(
    type: Type,
    tabIndex: String? = null,
    placeHolder: String? = null,
    value: String
  ) {
    val input = TagElement(appendable, "input")
      .startStarterTag()
    //attributes
    appendable.append(" type=${type.label}")
    if (tabIndex != null) {
      appendClient(" tabIndex={$tabIndex}")
      appendServer(""" tabindex="$tabIndex"""")
    }
    if (placeHolder != null) appendable.append(""" placeholder="$placeHolder"""")
    appendable.append(""" value="$value"""")
    input.endStarterTag().endTag()
  }

}
