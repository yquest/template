package pt.fabm.tpl.test

class CarListClient(appendable: Appendable) : CarList(appendable) {
  private val root = TagElement(appendable,"div")
  override fun colspanTr(colspan: Int): String = """colSpan={$colspan}"""
  override fun carLines() {
    appendable.append("{carView.carViewList()}")
  }

  override fun start(className:String) {
    root.appendStart(""" className="$className"""")
  }

  override fun end() {
    root.appendEnd()
  }

  override fun showIfAuthenticated(block: TagElement.() -> Unit) {
    appendable.append("{stores.user.authenticated && (")
    root.block()
    appendable.append(")}")
  }

}
