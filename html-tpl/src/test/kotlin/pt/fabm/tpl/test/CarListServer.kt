package pt.fabm.tpl.test

class CarListServer(appendable: Appendable, private val auth:Boolean, private val cars:List<CarFields>) : CarList(appendable) {
  private val root = TagElement(appendable,"div")
  override fun colspanTr(colspan: Int): String = """colspan="$colspan""""
  override fun carLines() {
    cars.forEach {
      CarView.render({CarViewServer(auth, appendable)},cars)
    }
  }

  override fun start(className:String) {
    root.appendStart(""" class="$className"""")
  }

  override fun end() {
    root.appendEnd()
  }

  override fun showIfAuthenticated(block: TagElement.() -> Unit) {
    if(auth) root.block()
  }

}
