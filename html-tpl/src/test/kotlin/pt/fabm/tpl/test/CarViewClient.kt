package pt.fabm.tpl.test

class CarViewClient(appendable: Appendable) : CarView(appendable) {
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

  override fun i(className: String) {
    val i = TagElement(appendable, "i")
    i.appendStart(""" className="$className"""").appendEnd()
  }

  override fun a(className: String, onClick: String?, block: CarView.() -> Unit) {
    val a = TagElement(appendable, "a")
    a.appendStart(""" href="" className="$className" onClick={$onClick}""")
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
