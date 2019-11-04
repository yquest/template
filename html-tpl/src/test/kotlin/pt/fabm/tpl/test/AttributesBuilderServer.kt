package pt.fabm.tpl.test

class AttributesBuilderServer : AttributesBuilder() {
  override fun appendClientAttr(key: String, value: String, wrapper: (String) -> String): AttributesBuilder {
    return this
  }

  override fun classVarAttr(value: String): AttributesBuilder {
    //ignore on server
    return this
  }

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

  override fun appendClientAttr(value: String): AttributesBuilder {
    //ignore on server
    return this
  }

  override fun appendServerAttr(value: String): AttributesBuilder {
    append(value)
    return this
  }

  override fun asServer(key: String, value: String): String {
    return if(value.isEmpty()) "" else " $value"
  }
  override fun asClient(key: String, value: String, wrapper: (String) -> String): String {
    return ""
  }

  override fun appendAttr(
    keyServer: String,
    keyClient: String,
    valueServer: String,
    valueClient: String,
    clientWrapper: (String) -> String
  ) {
    append(""" $keyServer="$valueServer"""")
  }

  override fun appendServerAttr(key: String, value: String): AttributesBuilder {
    append(""" $key="$value"""")
    return this
  }
}
