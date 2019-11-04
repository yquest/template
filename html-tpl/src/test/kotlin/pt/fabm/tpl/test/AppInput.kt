package pt.fabm.tpl.test

abstract class AppInput(appendable: Appendable) : Element(appendable) {
  enum class Type(val label: String) {
    TEXT("text"),
    PASSWORD("password")
  }

  fun render(
    label: String,
    type: Type,
    value: String,
    tabIndex: Int? = null,
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

  private fun appendElement(attributes: String = "", name: String, block: AppInput.() -> Unit) {
    val element = TagElement(appendable, name)
    element.appendStart(attributes)
    this.block()
    element.appendEnd()
  }

  fun div(className: String, block: AppInput.() -> Unit) = appendElement(
    attributes = attributesBuilder.classNameAttr(className).build(),
    name = "div",
    block = block
  )

  abstract val attributesBuilder: AttributesBuilder
  fun typeHelper(type: Type): String = """ type="${type.label}""""
  fun valueHelper(value: String): String = """ value="$value""""
  fun placeHolderEval(value: String?) = if (value == null) "" else """ placeholder="$value""""
  internal fun label(block: AppInput.() -> Unit) {
    val label = TagElement(appendable, "label").appendStart()
    this.block()
    label.appendEnd()
  }

  internal fun input(
    type: Type,
    tabIndex: Int? = null,
    placeHolder: String? = null,
    value: String
  ) {
    TagElement(appendable, "input")
      .appendStart(
        attributesBuilder
          .tabIndexEval(tabIndex)
          .append(placeHolderEval(placeHolder))
          .append(typeHelper(type))
          .append(valueHelper(value))
          .build()
      ).appendEnd()
  }

}
