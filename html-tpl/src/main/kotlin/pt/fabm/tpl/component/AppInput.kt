package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

abstract class AppInput(buffer: Buffer) : Element(buffer), MultiEnvTemplate {
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
    val div = TagElement(buffer, "div").startStarterTag()
    //attributes
    appendClassName(className)
    div.endStarterTag()
    this.block()
    div.endTag()
  }

  private fun label(block: AppInput.() -> Unit) {
    val label = TagElement(buffer, "label").noAttributesStarterTag()
    this.block()
    label.endTag()
  }

  internal abstract fun input()

}
