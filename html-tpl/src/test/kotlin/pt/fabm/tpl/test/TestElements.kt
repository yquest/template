package pt.fabm.tpl.test


open class Element(val appendable: Appendable){
  fun appendBody(body: String):Element{
    appendable.append(body)
    return this
  }
  operator fun String.unaryPlus(){
    appendBody(this)
  }
}

open class TagElement(appendable: Appendable, private val name:String):Element(appendable){
  fun appendStart(attributes: String=""):TagElement{
    appendable.append("<$name$attributes>")
    return this
  }
  fun appendEnd():TagElement{
    appendable.append("</$name>")
    return this
  }
}
