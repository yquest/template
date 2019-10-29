package pt.fabm.tpl.test

class HelperServer: Helper() {
  //render nothing in server
  override fun onClickAttr(value: String): String =""
  //render nothing in server
  override fun onClickEval(value: String?): String =""
  override fun colSpanEval(value: Int?): String = if(value == null) "" else """ colSpan=$value """

  override fun tabIndexEval(value: Int?): String = if(value == null) "" else tabIndexAttr(value)
  override fun tabIndexAttr(value: Int): String = """ tabindex="$value""""
  override fun classNameEval(value: String?): String = if(value == null) "" else classNameAttr(value)
  override fun classNameAttr(value: String): String = """ class="$value""""
}
