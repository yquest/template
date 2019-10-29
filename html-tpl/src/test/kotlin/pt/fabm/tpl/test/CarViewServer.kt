package pt.fabm.tpl.test

class CarViewServer(private val auth: Boolean, appendable: Appendable) : CarView(appendable) {
  override val helper: Helper = HelperServer()
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
}
