package pt.fabm.tpl.test

class AppInputServer(appendable: Appendable) : AppInput(appendable) {
  override fun start(className: String) {
    root.appendStart(""" class="$className"""")
  }

  override fun end() {
    root.appendEnd()
  }

  override fun label(block: AppInput.() -> Unit) {
    val label = TagElement(appendable,"label")
    label.appendStart()
    block()
    label.appendEnd()
  }

  override fun input(type: String, tabIndex: Int?, placeHolder: String?, value: String) {
    fun placeHolderRes():String = if(placeHolder == null) "" else """ placeholder="$placeHolder""""
    fun tabIndexRes():String = if(tabIndex == null) "" else """ tabindex="$tabIndex""""

    val input = TagElement(appendable,"input")
    input.appendStart(""" type="$type" value="$value"${placeHolderRes()}${tabIndexRes()}""")
    input.appendEnd()
  }

}
