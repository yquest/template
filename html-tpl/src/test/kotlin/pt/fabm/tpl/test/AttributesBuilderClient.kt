package pt.fabm.tpl.test

class AttributesBuilderClient : AttributesBuilder() {
  private fun fnTabIndexAttr(value: Int): String = " tabIndex={$value}"
  private fun fnClassNameAttr(value: String): String = """ className="$value""""
  private fun fnOnClickEval(value: String?): String = if (value == null) "" else """ """



  override fun colSpanEval(value: Int?): AttributesBuilder {
    builder.append(
      if (value == null) "" else """ colSpan=$value """
    )
    return this
  }

  override fun tabIndexEval(value: Int?): AttributesBuilder {
    builder.append(
      if (value == null) "" else fnTabIndexAttr(value)
    )
    return this
  }

  override fun tabIndexAttr(value: Int): AttributesBuilder {
    builder.append(fnTabIndexAttr(value))
    return this
  }

  override fun classNameEval(value: String?): AttributesBuilder {
    builder.append(
      if (value == null) "" else fnClassNameAttr(value)
    )
    return this
  }

  override fun classNameAttr(value: String): AttributesBuilder {
    builder.append(""" className="$value"""")
    return this
  }

  override fun onClickEval(value: String?): AttributesBuilder {
    builder.append(if (value == null) "" else fnOnClickEval(value))
    return this
  }

  override fun onClickAttr(value: String): AttributesBuilder {
    builder.append(""" onClick={$value}""")
    return this
  }
}
