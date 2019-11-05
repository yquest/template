package pt.fabm.tpl.test

class DropDowInputServer(
  appendable: Appendable,
  override val nameInput: String,
  val isDisabledButton: Boolean,
  override val list: List<String>
) :
  DropDownInput(appendable) {
  override val disabledButton: String get() = if (isDisabledButton) """ disabled="disabled"""" else ""
  private var currentItemText: String? = null
  override val itemText: String get() = currentItemText!!
  override val classNameIsOpen: String = """ class="dropdown-menu""""

  override fun clientText(text: String): String = ""

  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()
  fun render(label: String, btnLabel: String, value: Int, tabIndex: Int) {
    super.render(label = label, btnLabel = btnLabel, value = list[value], tabIndex = tabIndex.toString())
  }

  override fun eachItem(block: DropDownInput.(idx: Int, listItem: String) -> Unit) {
    list.forEachIndexed { idx, item ->
      currentItemText = item
      this.block(idx, item)
    }
  }

  override fun showIfError(block: DropDownInput.() -> Unit) {
    this.block()
  }

  override fun appendClient(text: String) {
    //ignore client
  }

  override fun appendServer(text: String) {
    appendBody(text)
  }
}
