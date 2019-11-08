package pt.fabm.tpl.component

open class Element(val appendable: Appendable) {
  fun appendBody(body: String): Element {
    appendable.append(body)
    return this
  }

  operator fun String.unaryPlus() {
    appendBody(this)
  }

  fun emptyHref(){
    appendable.append(""" href=""""")
  }
}
