package pt.fabm.tpl.test

class HelperClient: Helper() {
  override fun colSpanEval(value: Int?) = if(value == null) "" else """ colSpan=$value """
  override fun tabIndexEval(value: Int?): String = if(value==null) "" else tabIndexAttr(value)
  override fun tabIndexAttr(value: Int): String = " tabIndex={$value}"
  override fun classNameEval(value: String?): String = if(value == null) "" else classNameAttr(value)
  override fun classNameAttr(value: String): String = """ className="$value""""
  override fun onClickEval(value: String?):String = if(value == null) "" else """ """
  override fun onClickAttr(value: String):String = """ onClick={$value}"""
}
