package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

open class Element(val buffer: Buffer) {
  fun appendBody(body: String): Element {
    buffer.appendString(body)
    return this
  }

  operator fun String.unaryPlus() {
    appendBody(this)
  }

  fun emptyHref(){
    buffer.appendString(""" href=""""")
  }
}
