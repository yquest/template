package pt.fabm.tpl.test

class CarViewClient(appendable: Appendable) : CarView(appendable) {
  override val attributesBuilder: AttributesBuilder = AttributesBuilderClient()

  override fun start() {
    root.appendStart(" className={props.classes}")
  }

  override fun end() {
    root.appendEnd()
  }

  override fun showIfAuthenticated(block: CarView.() -> Unit) {
    appendable.append("""{props.authenticated && (""")
    block()
    appendable.append(")}")
  }

  override fun showIfBlockedRemove(block: CarView.() -> Unit) {
    appendable.append("""{props.blockedRemove &&(""")
    block()
    appendable.append(")}")
  }

  override fun showIfBlockedNotRemove(block: CarView.() -> Unit) {
    appendable.append("""{!props.blockedRemove &&(""")
    block()
    appendable.append(")}")
  }

}
