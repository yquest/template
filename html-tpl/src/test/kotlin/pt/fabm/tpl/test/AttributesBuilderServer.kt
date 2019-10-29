package pt.fabm.tpl.test

class AttributesBuilderServer : AttributesBuilder() {
  private fun fnTabIndexAttr(value: Int): String = """ tabindex="$value""""
  private fun fnClassNameAttr(value: String): String = """ class="$value""""
  //render nothing in server
  override fun onClickAttr(value: String): AttributesBuilder {
    return this
  }

  //render nothing in server
  override fun onClickEval(value: String?): AttributesBuilder {
    return this
  }

  override fun colSpanEval(value: Int?): AttributesBuilder {
    builder.append(
      if (value == null) "" else """ colspan=$value """
    )
    return this
  }

  override fun tabIndexEval(value: Int?): AttributesBuilder{
    builder.append(
      if (value == null) "" else fnTabIndexAttr(value)
    )
    return this
  }
  override fun tabIndexAttr(value: Int): AttributesBuilder{
    builder.append(
      fnTabIndexAttr(value)
    )
    return this
  }

  override fun classNameEval(value: String?): AttributesBuilder {
    builder.append(
      if (value == null) "" else fnClassNameAttr(value)
    )
    return this
  }
  override fun classNameAttr(value: String): AttributesBuilder{
    builder.append(
      fnClassNameAttr(value)
    )
    return this
  }
}
