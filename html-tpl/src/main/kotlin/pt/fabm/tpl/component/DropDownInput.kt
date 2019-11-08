package pt.fabm.tpl.component

abstract class DropDownInput(appendable: Appendable) : Element(appendable), MultiEnvTemplate {

  internal fun render(
    label: String,
    btnLabel: String,
    value: String,
    tabIndex: String
  ) {
    div(className = "form-group", onKeyDown = "props.keyDown") {
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
          div(classNameIsOpen) {
            eachItem {
              a(
                key = "item",
                onClick = "props.onSelectItem(idx)",
                className = TemplateTuple(
                  server = "dropdown-item",
                  client = "props.itemClasses(idx)"
                ),
                mouseDown = "event => event.preventDefault()"
              ) {
                +itemText
              }
            }
          }
        }
        input(
          name = nameInput,
          value = value,
          type = AppInput.Type.TEXT
        )
        showIfError {
          div(className = "invalid-feedback") {
            +clientText("{props.error}")
          }
        }
      }
    }
  }

  abstract val classNameIsOpen: String
  abstract val itemText: String
  abstract val nameInput: String
  abstract val disabledButton: String
  abstract val list: List<String>

  abstract fun clientText(text: String): String
  abstract fun eachItem(block: DropDownInput.() -> Unit)
  abstract fun showIfError(block: DropDownInput.() -> Unit)

  fun div(attr:String, block: DropDownInput.() -> Unit){
    val div = TagElement(appendable, "div")
    div.startStarterTag()
    appendBody(attr)
    div.endStarterTag()
    this.block()
    div.endTag()
  }

  fun div(
    className: String? = null,
    onKeyDown: String? = null,
    block: DropDownInput.() -> Unit
  ) {
    val div = TagElement(appendable, "div")
    div.startStarterTag()
    if (className != null) {
      appendClient(""" className="$className"""")
      appendServer(""" class="$className"""")
    }
    if (onKeyDown != null) {
      appendClient(" onKeyDown={$onKeyDown}")
    }
    div.endStarterTag()
    this.block()
    div.endTag()
  }

  internal fun a(
    key: String,
    mouseDown: String,
    onClick: String,
    className: TemplateTuple,
    block: DropDownInput.() -> Unit
  ) {
    val a = TagElement(appendable, "a").startStarterTag()
    emptyHref()
    appendClient(""" key={$key} onMouseDown={$mouseDown} onClick={$onClick} className={${className.client}}""")
    appendServer(""" class="${className.server}"""")

    a.endStarterTag()
    this.block()
    a.endTag()
  }

  internal fun label(block: DropDownInput.() -> Unit) {
    val label = TagElement(appendable, "label").noAttributesStarterTag()
    this.block()
    label.endTag()
  }

  internal fun button(
    className: String,
    onClick: String,
    onBlur: String,
    tabIndex: String,
    type: String,
    block: DropDownInput.() -> Unit
  ) {
    val button = TagElement(appendable, "button").startStarterTag()
    appendBody(disabledButton)
    appendClient(""" onBlur={$onBlur} tabIndex={$tabIndex} type="$type" onClick={$onClick} className="$className" """)
    appendServer(""" tabindex="$tabIndex" type="$type" class="$className"""")
    button.endStarterTag()
    this.block()
    button.endTag()
  }

  internal fun input(
    name: String,
    type: AppInput.Type,
    value: String
  ) {
    val input = TagElement(appendable, "input")
      .startStarterTag()
    appendBody(""" type="${type.label}" name=$name value=$value""")
    input.endStarterTag()
  }
}
