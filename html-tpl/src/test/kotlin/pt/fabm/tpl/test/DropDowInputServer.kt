package pt.fabm.tpl.test

class DropDowInputServer(appendable: Appendable, override val list: List<String>) : DropDownInput(appendable,false) {
  override fun clientText(text: String): String = ""
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()

  fun render(label: String, btnLabel: String, value: Int, tabIndex: Int) {
    super.render(label = label, btnLabel = btnLabel, value = list[value], tabIndex = tabIndex.toString())
  }
  override fun eachItem(block: DropDownInput.(idx: Int, listItem: String) -> Unit) {
    list.forEachIndexed{idx,item->
      this.block(idx,item)
    }
  }

  override fun showIfError(block: DropDownInput.() -> Unit) {
    this.block()
  }
}
