package pt.fabm.tpl.test

class AppInputServer(appendable: Appendable) : AppInput(appendable) {
  override val attributesBuilder: AttributesBuilder = AttributesBuilderServer()
}
