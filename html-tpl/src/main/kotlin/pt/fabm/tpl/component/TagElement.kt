package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

open class TagElement(
  appendable: Buffer,
  private val name: String
) :
  Element(appendable) {

  fun createSingleTag():TagElement{
    buffer.appendString("<$name></$name>")
    return this
  }

  fun noAttributesStarterTag(): TagElement {
    buffer.appendString("<$name>")
    return this
  }

  fun startStarterTag(): TagElement {
    buffer.appendString("<$name")
    return this
  }

  fun endStarterTag():TagElement{
    buffer.appendString(">")
    return this
  }

  fun <T>doContainedBlock(blockObj:T,block:T.()->Unit):TagElement{
    blockObj.block()
    return this
  }

  fun endTag(): TagElement {
    buffer.appendString("</$name>")
    return this
  }
}
