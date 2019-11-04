package pt.fabm.tpl.test

abstract class DropDownInput(appendable: Appendable) : Element(appendable), MultiEnvTemplate {

  internal fun render(
    label: String,
    btnLabel: String,
    value: String,
    tabIndex: String
  ) {
    div(className = "form-group", client = " onKeyDown={props.keyDown}") {
      label { +label }
      div(className = "input-group") {
        div(className = "input-group-prepend") {
          button(
            tabIndex = tabIndex,
            className = "btn btn-outline-secondary dropdown-toggle",
            onClick = "props.toggle",
            onBlur = "props.blur",
            type = "button"
          ) { +btnLabel }
          div(client = " className={props.classesIsOpen}", server = """ class="dropdown-menu"""") {
            eachItem { item, idx ->
              a(
                key = "item",
                onClick = "props.onSelectItem(idx)",
                className = TemplateTuple(
                  server = "dropdown-item",
                  client = "props.itemClasses(idx)"
                ),
                mouseDown = "event => event.preventDefault()"
              ) {
                +resolve(item.toString(), "{item}")
              }
            }
          }
        }
        input(value = value, type = AppInput.Type.TEXT)
        showIfError {
          div(className = "invalid-feedback") {
            +clientText("{props.error}")
          }
        }
      }
    }
  }

  private fun appendElement(attributes: Appendable = StringBuilder(), name: String, block: DropDownInput.() -> Unit) {
    val element = TagElement(appendable, name)
    element.startStarterTag()
    this.block()
    element.endTag()
  }

  abstract fun resolve(serverText: String, clientText: String): String
  abstract fun clientText(text: String): String
  abstract fun eachItem(block: DropDownInput.(idx: Int, listItem: String) -> Unit)
  abstract fun showIfError(block: DropDownInput.() -> Unit)

  fun div(className: String? = null, client: String = "", server: String = "", block: DropDownInput.() -> Unit) =
    appendElement(
      attributes = attributesBuilder
        .classNameEval(className)
        .appendClientAttr(client)
        .appendServerAttr(server)
        .builder,
      name = "div",
      block = block
    )

  abstract val attributesBuilder: AttributesBuilder
  abstract val list: List<String>

  internal fun a(
    key: String,
    mouseDown: String,
    onClick: String,
    className: TemplateTuple,
    block: DropDownInput.() -> Unit
  ) = appendElement(
    attributes = attributesBuilder
      .appendClientAttr(
        """ key={$key} onMouseDown={$mouseDown} onClick={$onClick} className={${className.client}}"""
      )
      .appendServerAttr(""" class="${className.server}"""")
      .emptyHref()
      .builder,
    name = "a",
    block = block
  )

  internal fun label(block: DropDownInput.() -> Unit) {
    val label = TagElement(appendable, "label").startStarterTag()
    this.block()
    label.endTag()
  }

  private fun AttributesBuilder.disabledButton(): AttributesBuilder {
    if (this is AttributesBuilderClient) append(""" disabled={props.disabled}""")
    return this
  }

  internal fun button(
    className: String,
    onClick: String,
    onBlur: String,
    tabIndex: String,
    type: String,
    block: DropDownInput.() -> Unit
  ) {
    val attributes = attributesBuilder
      .clear()
      .disabledButton()
      .appendClientAttr(key = "onBlur", value = onBlur, wrapper = AttributesBuilder.VAR_WRAPPER)
      .appendServerAttr("tabindex", tabIndex)
      .appendClientAttr("tabIndex", tabIndex, AttributesBuilder.VAR_WRAPPER)
      .appendClientAttr(" type=\"$type\"")
      .appendServerAttr(" type=\"$type\"")
      .classNameAttr(className)
      .onClickAttr(onClick)
      .builder
    val label = TagElement(appendable, "button")
      .startStarterTag()
    this.block()
    label.endTag()
  }

  internal fun input(
    name: TemplateTuple,
    type: AppInput.Type,
    value: String
  ) {
    val attributes = attributesBuilder
      .clear()
      .append(""" type="${type.label}"""")
      .appendClientAttr(""" name={${name.client}}""")
      .append()
      .builder
    TagElement(appendable, "input")
      .startStarterTag().endTag()
  }
}
