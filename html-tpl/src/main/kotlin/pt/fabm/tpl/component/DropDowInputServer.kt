package pt.fabm.tpl.component

class DropDowInputServer(
  appendable: Appendable,
  override val nameInput: String,
  val isDisabledButton: Boolean,
  override val list: List<String>
) :
  DropDownInput(appendable), MultiEnvTemplateServer {
  override val disabledButton: String get() = if (isDisabledButton) """ disabled="disabled"""" else ""
  private var currentItemText: String? = null
  override val itemText: String get() = currentItemText!!
  override val classNameIsOpen: String = """ class="dropdown-menu""""

  override fun clientText(text: String): String = ""

  fun render(label: String, btnLabel: String, value: Int, tabIndex: Int) {
    super.render(label = label, btnLabel = btnLabel, value = list[value], tabIndex = tabIndex.toString())
  }

  override fun eachItem(block: DropDownInput.() -> Unit) {
    list.forEach { item ->
      currentItemText = item
      this.block()
    }
  }

  override fun showIfError(block: DropDownInput.() -> Unit) {
    this.block()
  }
}
