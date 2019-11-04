package pt.fabm.tpl.test

class DropDowInputClient(appendable: Appendable) : DropDownInput(appendable) {
  override val list: List<String> = listOf("item")

  override val attributesBuilder: AttributesBuilder = AttributesBuilderClient()

  fun render(){
    super.render(
      label = "{props.label}",
      tabIndex = "props.tabIndex",
      value = "{props.inputValue}",
      btnLabel = "{props.btnLb}"
    )
  }
  override fun eachItem(block: DropDownInput.(idx: Int, listItem: String) -> Unit) {
    appendBody("{props.labels.map((item, idx) => (")
    this.block(0,"idx")
    appendBody("))}")
  }
  override fun showIfError() {
    appendBody("{props.error !== null && props.error.length > 0 && (")
    appendBody("<div className=\"invalid-feedback\">{props.error}</div>")
    appendBody(")}")
  }
}
