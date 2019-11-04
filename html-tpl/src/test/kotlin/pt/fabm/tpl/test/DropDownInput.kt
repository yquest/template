package pt.fabm.tpl.test

abstract class DropDownInput(appendable: Appendable) : Element(appendable) {

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
            onBlur = "props.blur"
          ) { +btnLabel }
          div(client = " className={props.classesIsOpen}", server = """ class="dropdown-menu"""") {
            eachItem { idx, item ->
              a(
                key = item,
                onClick = "props.onSelectItem(idx)",
                className = TemplateTuple(
                  server = "dropdown-item",
                  client = "props.itemClasses(idx)"
                ),
                mouseDown = "event => event.preventDefault()"
              ) { +item }
            }
          }
        }
        input(value = value)
        showIfError()

      }
    }
  }

  private fun appendElement(attributes: String = "", name: String, block: DropDownInput.() -> Unit) {
    val element = TagElement(appendable, name)
    element.appendStart(attributes)
    this.block()
    element.appendEnd()
  }

  abstract fun eachItem(block: DropDownInput.(idx: Int, listItem: String) -> Unit)
  abstract fun showIfError()

  fun div(className: String? = null, client: String = "", server: String = "", block: DropDownInput.() -> Unit) =
    appendElement(
      attributes = attributesBuilder
        .classNameEval(className)
        .appendClientAttr(client)
        .appendServerAttr(server)
        .build(),
      name = "div",
      block = block
    )

  abstract val attributesBuilder: AttributesBuilder
  abstract val list:List<String>


  internal fun a(
    key: String,
    mouseDown: String,
    onClick: String,
    className: TemplateTuple,
    block: DropDownInput.() -> Unit
  ) = appendElement(
      attributes = attributesBuilder
        .appendClientAttr(
          """ key="$key" onMouseDown={$mouseDown} onClick={$onClick} className={${className.client}}"""
        )
        .appendServerAttr(""" class="${className.server}"""")
        .emptyHref()
        .build(),
      name = "a",
      block = block
    )

  internal fun label(block: DropDownInput.() -> Unit) {
    val label = TagElement(appendable, "label").appendStart()
    this.block()
    label.appendEnd()
  }

  internal fun button(
    className: String,
    onClick: String,
    onBlur: String,
    tabIndex: String,
    block: DropDownInput.() -> Unit
  ) {
    val label = TagElement(appendable, "button")
      .appendStart(
        attributesBuilder
          .appendClientAttr(key = "onBlur", value = onBlur, wrapper = AttributesBuilder.VAR_WRAPPER)
          .appendServerAttr("tabindex", tabIndex)
          .appendClientAttr("tabIndex", tabIndex, AttributesBuilder.VAR_WRAPPER)
          .classNameAttr(className)
          .onClickAttr(onClick)
          .build()
      )
    this.block()
    label.appendEnd()
  }

  internal fun input(
    value: String
  ) {
    TagElement(appendable, "input")
      .appendStart(
        attributesBuilder
          .appendServerAttr("value", value)
          .appendClientAttr("value", value)
          .build()
      ).appendEnd()
  }
}
