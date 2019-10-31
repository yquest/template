package pt.fabm.tpl.test

abstract class AppInput(appendable: Appendable) : Element(appendable) {
  enum class Type(val label: String) {
    TEXT("text"),
    PASSWORD("password")
  }

  companion object {
    fun render(
      label: String,
      type: Type,
      value: String,
      tabIndex: Int? = null,
      placeHolder: String? = null,
      appInputCreator: () -> AppInput
    ) {
      fun appInput(className: String, block: AppInput.() -> Unit) {
        val appInput = appInputCreator()
        appInput.attributesBuilder.builder.clear()
        appInput.attributesBuilder.classNameAttr(className)
        appInput.root.appendStart(appInput.attributesBuilder.toString())
        appInput.block()
        appInput.root.appendEnd()
      }

      appInput(className = "form-group") {
        label { +label }
        input(
          type = type,
          tabIndex = tabIndex,
          placeHolder = placeHolder,
          value = value
        )
      }
    }
  }

  internal val root = TagElement(appendable, "div")
  abstract val attributesBuilder:AttributesBuilder
  fun typeHelper(type:Type):String = """ type="${type.label}""""
  fun valueHelper(value:String):String = """ value="$value""""
  fun placeHolderEval(value: String?) = if(value == null) "" else """ placeholder="$value""""
  internal fun label(block: AppInput.() -> Unit){
    val label = TagElement(appendable,"label").appendStart()
    this.block()
    label.appendEnd()
  }
  internal fun input(
    type: Type,
    tabIndex: Int? = null,
    placeHolder: String? = null,
    value: String
  ){
    attributesBuilder.builder.clear()
    attributesBuilder.tabIndexEval(tabIndex)
    attributesBuilder.builder.append(placeHolderEval(placeHolder))
    attributesBuilder.builder.append(typeHelper(type))
    attributesBuilder.builder.append(valueHelper(value))

    TagElement(appendable,"input")
      .appendStart(attributesBuilder.builder.toString())
      .appendEnd()
  }

}
