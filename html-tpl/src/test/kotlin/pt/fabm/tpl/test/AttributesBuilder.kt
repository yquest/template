package pt.fabm.tpl.test

abstract class AttributesBuilder {
  internal val builder = StringBuilder()
  fun emptyHref():AttributesBuilder {
    builder.append(""" href=""""")
    return this
  }
  abstract fun classNameEval(value: String?): AttributesBuilder
  abstract fun classNameAttr(value: String): AttributesBuilder
  abstract fun tabIndexEval(value: Int?): AttributesBuilder
  abstract fun tabIndexAttr(value: Int): AttributesBuilder
  abstract fun onClickAttr(value: String): AttributesBuilder
  abstract fun onClickEval(value: String?): AttributesBuilder
  abstract fun colSpanEval(value: Int?): AttributesBuilder
}
