package pt.fabm.tpl.test

abstract class AppInput(appendable: Appendable) : Element(appendable) {
  enum class Type(val label: String) {
    TEXT("text"),
    PASSWORD("password")
  }

  companion object {
    fun render(
      label: String,
      type: String,
      value: String,
      tabIndex: Int? = null,
      placeHolder: String? = null,
      appInputCreator: () -> AppInput
    ) {
      fun appInput(className: String, block: AppInput.() -> Unit) {
        val appInput = appInputCreator()
        appInput.start(className)
        appInput.block()
        appInput.end()
      }

      appInput(className = "form-group") {
        label { +label }
        input(
          type = type,
          tabIndex = tabIndex,
          placeHolder = placeHolder,
          value = value
        )
      }
    }
  }

  protected val root = TagElement(appendable, "div")
  abstract fun start(className: String)
  abstract fun end()
  abstract fun label(block: AppInput.() -> Unit)
  abstract fun input(
    type: String,
    tabIndex: Int? = null,
    placeHolder: String? = null,
    value: String
  )

}
