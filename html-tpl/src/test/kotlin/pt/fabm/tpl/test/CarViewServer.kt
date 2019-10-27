package pt.fabm.tpl.test

class CarViewServer(private val auth: Boolean, appendable: Appendable) : CarView(appendable) {

  override fun start() {
    root.appendStart()
  }

  override fun end() {
    root.appendEnd()
  }

  override fun showIfAuthenticated(block: CarView.() -> Unit) {
    if (auth) block()
  }

  override fun showIfBlockedRemove(block: CarView.() -> Unit) {
    //ignore
  }

  override fun showIfBlockedNotRemove(block: CarView.() -> Unit) {
    //do always
    block()
  }

  override fun i(className: String) {
    val i = TagElement(appendable, "i")
    i.appendStart(""" class="$className"""").appendEnd()
  }

  override fun a(className: String, onClick: String?, block: CarView.() -> Unit) {
    val a = TagElement(appendable, "a")
    a.appendStart(""" href="" class="$className"""")
    block()
    a.appendEnd()
  }

  override fun td(block: CarView.() -> Unit) {
    val td = TagElement(appendable, "td")
    td.appendStart()
    block()
    td.appendEnd()
  }
}
