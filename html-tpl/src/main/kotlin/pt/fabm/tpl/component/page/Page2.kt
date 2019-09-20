package pt.fabm.tpl.component.page

import pt.fabm.tpl.*
import pt.fabm.tpl.component.app.AppInput

class Page2(
  override val type: Type,
  auth: Boolean = true,
  edit: Boolean = true,
  username: () -> String = { "" },
  input1: () -> String = { "" },
  input2: () -> String = { "" }
) :
  Page(type, auth, edit, username) {
  private val component: DIV = DIV(type) { "" }
  override fun createComponent(): DIV = component

  init {
    fun initPage2(init: DIV.() -> Unit) {
      component.init()
    }

    fun TagWithText.input(
      placeHolder: String,
      tabIndex: Int,
      onChange: String,
      label: String,
      value: Pair<() -> String, String>,
      error: String
    ) {
      this.children += AppInput(
        tabIndex = tabIndex,
        placeholder = placeHolder,
        typeInput = "text",
        label = label,
        onChange = onChange,
        value = value,
        type = type.toFirstLevel(),
        error = error
      )
    }

    fun TagWithText.submitButton() {
      this.children +=
        object : TagWithText("button", type) {
          override val children: MutableList<ElementCreator> = mutableListOf()
          override fun create(): Element {
            val className = if (type == Type.SERVER) "class" else "className"
            val tabIndex = if (type == Type.SERVER) "tabindex" else "tabIndex"
            val tabIndexValue = if (type == Type.SERVER) """"3"""" else "{3}"
            return TagElement(
              name, listOf(
                TextElement("submit")
              )
            ) { """ $className="btn btn-primary" $tabIndex=$tabIndexValue """ }
          }

        }
    }

    initPage2 {
      div { +"2nd page" }
      form(onSubmitEvent = "submitFormEvent") {
        input(
          tabIndex = 1,
          placeHolder = "put something here",
          label = "my label",
          onChange = "updateValue(Control.INPUT1)",
          value = input1 to "form1.input[Control.INPUT1]",
          error = "form1.error[Control.INPUT1]"
        )
        input(
          tabIndex = 2,
          placeHolder = "put something also here",
          label = "my label 2",
          onChange = "updateValue(Control.INPUT2)",
          value = input2 to "form1.input[Control.INPUT2]",
          error = "form1.error[Control.INPUT2]"
        )
        submitButton()
      }
    }
  }
}
