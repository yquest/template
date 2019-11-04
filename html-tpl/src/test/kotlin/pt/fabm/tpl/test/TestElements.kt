package pt.fabm.tpl.test


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

open class TagElement(
  appendable: Appendable,
  private val name: String
) :
  Element(appendable) {

  fun createSingleTag():TagElement{
    appendable.append("<$name></$name>")
    return this
  }

  fun noAttributesStarterTag(): TagElement {
    appendable.append("<$name>")
    return this
  }

  fun startStarterTag(): TagElement {
    appendable.append("<$name")
    return this
  }

  fun endStarterTag():TagElement{
    appendable.append(">")
    return this
  }

  fun appendAttribute(attribute:String):TagElement{
    appendable.append(attribute)
    return this
  }

  fun <T>doContainedBlock(blockObj:T,block:T.()->Unit):TagElement{
    blockObj.block()
    return this
  }

  fun endTag(): TagElement {
    appendable.append("</$name>")
    return this
  }
}
