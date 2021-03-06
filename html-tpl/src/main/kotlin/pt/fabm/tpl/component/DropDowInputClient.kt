package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

class DropDowInputClient(buffer: Buffer) : DropDownInput(buffer),
  MultiEnvTemplateClient {
  override val disabledButton: String = " disabled={props.disabled}"
  override val nameInput: String = "{props.inputName}"
  override val itemText: String = "{item}"
  override val classNameIsOpen: String = " className={props.classesIsOpen}"
  override val list: List<String> = listOf("item")

  override fun clientText(text: String): String = text
  fun render(){
    super.render(
      label = "{props.label}",
      tabIndex = "props.tabIndex",
      value = "{props.inputValue}",
      btnLabel = "{props.btnLb}"
    )
  }

  override fun eachItem(block: DropDownInput.() -> Unit) {
    appendBody("{props.labels.map((item, idx) => (")
    this.block()
    appendBody("))}")
  }
  override fun showIfError(block: DropDownInput.() -> Unit) {
    appendBody("{props.error !== null && props.error.length > 0 && (")
    this.block()
    appendBody(")}")
  }
}
