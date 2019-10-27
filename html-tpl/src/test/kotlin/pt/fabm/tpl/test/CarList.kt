package pt.fabm.tpl.test

abstract class CarList(appendable: Appendable) : Element(appendable) {
  companion object{
    fun render(carListCreator:()->CarList){
      fun carList(className: String, block: CarList.() -> Unit) {
        val carList = carListCreator()
        carList.start(className)
        carList.block()
        carList.end()
      }

      carList(className = "form-group") {
        table {
          thead {
            tr {
              th { +"Make" }
              th { +"Model" }
              th { +"Maturity date" }
              th { +"Price" }
              showIfAuthenticated {
                th(colspan = 2) { +"Actions" }
              }
            }
          }
          tbody {
            carLines()
          }
        }
      }
    }
  }

  abstract fun start(className:String)
  abstract fun end()

  private fun appendElement(attributes:String="",name:String,block:CarList.()->Unit){
    val element = TagElement(appendable,name)
    element.appendStart(attributes)
    block()
    element.appendEnd()
  }

  protected abstract fun colspanTr(colspan: Int):String

  fun table(block:CarList.()->Unit) = appendElement(name="table",block = block)
  fun tr(block:CarList.()->Unit) = appendElement(name="tr",block = block)
  fun thead(block:CarList.()->Unit) = appendElement(name="thead",block = block)
  fun tbody(block:CarList.()->Unit) = appendElement(name="tbody",block = block)
  fun th(colspan:Int?=null,block:CarList.()->Unit) = appendElement(
    name = "th",
    attributes = if(colspan == null) "" else """ ${colspanTr(colspan)}""",
    block = block
  )
  abstract fun carLines()
  abstract fun showIfAuthenticated(block:TagElement.()->Unit)

}
