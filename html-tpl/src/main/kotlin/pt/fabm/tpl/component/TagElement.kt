package pt.fabm.tpl.component

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

  fun <T>doContainedBlock(blockObj:T,block:T.()->Unit):TagElement{
    blockObj.block()
    return this
  }

  fun endTag(): TagElement {
    appendable.append("</$name>")
    return this
  }
}
