package pt.fabm.tpl.component

abstract class AppInput(appendable: Appendable) : Element(appendable), MultiEnvTemplate {
  enum class Type(val label: String) {
    TEXT("text"),
    PASSWORD("password")
  }

  internal fun render(label:String){
    div(className = "form-group") {
      label { +label }
      input()
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

  internal abstract fun input()

}
