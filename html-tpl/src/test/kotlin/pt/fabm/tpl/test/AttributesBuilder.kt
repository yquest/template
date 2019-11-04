package pt.fabm.tpl.test

abstract class AttributesBuilder {

  companion object {
    val CONST_WRAPPER: (String) -> String = { """"$it"""" }
    val VAR_WRAPPER: (String) -> String = { "{$it}" }
  }

  abstract fun asClient(key: String, value: String, wrapper: (String) -> String = CONST_WRAPPER): String
  abstract fun asServer(key: String, value: String): String

  internal val builder = StringBuilder()
  fun clear(): AttributesBuilder {
    builder.clear()
    return this
  }

  fun emptyHref(): AttributesBuilder {
    builder.append(""" href=""""")
    return this
  }

  fun append(value: String = ""): AttributesBuilder {
    builder.append(value)
    return this
  }

  abstract fun appendAttr(
    keyServer: String,
    keyClient: String,
    valueServer: String,
    valueClient: String,
    clientWrapper: (String) -> String
  )

  abstract fun appendClientAttr(
    key: String,
    value: String,
    wrapper: (String) -> String = CONST_WRAPPER
  ): AttributesBuilder

  abstract fun appendServerAttr(
    key: String,
    value: String
  ): AttributesBuilder

  abstract fun appendClientAttr(value: String): AttributesBuilder
  abstract fun appendServerAttr(value: String): AttributesBuilder
  abstract fun classNameEval(value: String?): AttributesBuilder
  abstract fun classNameAttr(value: String): AttributesBuilder
  abstract fun classVarAttr(value: String): AttributesBuilder
  abstract fun tabIndexEval(value: Int?): AttributesBuilder
  abstract fun tabIndexAttr(value: Int): AttributesBuilder
  abstract fun onClickAttr(value: String): AttributesBuilder
  abstract fun onClickEval(value: String?): AttributesBuilder
  abstract fun colSpanEval(value: Int?): AttributesBuilder
  fun build(): String {
    val str = builder.toString()
    builder.clear()
    return str
  }
}
