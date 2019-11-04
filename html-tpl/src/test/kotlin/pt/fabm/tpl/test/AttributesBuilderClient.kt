package pt.fabm.tpl.test

class AttributesBuilderClient : AttributesBuilder() {
  override fun asClient(key:String,value: String,wrapper:(String)->String): String {
    return if(value.isEmpty()) "" else """ $key=${wrapper(value)}"""
  }

  override fun asServer(key:String,value: String): String {
    return ""
  }

  private fun fnTabIndexAttr(value: Int): String = " tabIndex={$value}"

  private fun fnClassNameAttr(value: String): String = """ className="$value""""

  private fun fnOnClickAttr(value: String?): String = """ onClick={$value}"""

  override fun classVarAttr(value: String): AttributesBuilder {
    builder.append(" className={$value}")
    return this
  }

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
    builder.append(if (value == null) "" else fnOnClickAttr(value))
    return this
  }

  override fun onClickAttr(value: String): AttributesBuilder {
    builder.append(""" onClick={$value}""")
    return this
  }

  override fun appendClientAttr(value: String): AttributesBuilder {
    builder.append(value)
    return this
  }

  override fun appendServerAttr(value: String): AttributesBuilder {
    //ignore on client
    return this
  }

  override fun appendAttr(
    keyServer: String,
    keyClient: String,
    valueServer: String,
    valueClient: String,
    clientWrapper: (String) -> String
  ) {
    append("$keyClient=${clientWrapper(valueClient)}")
  }

  override fun appendClientAttr(key: String, value: String, wrapper: (String) -> String): AttributesBuilder {
    appendClientAttr(" $key=${wrapper(value)}")
    return this
  }

  override fun appendServerAttr(key: String, value: String): AttributesBuilder {
    //ignore
    return this
  }
}
