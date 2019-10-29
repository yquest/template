package pt.fabm.tpl.test

abstract class Helper{
  fun emptyHref() = """ href="""""
  abstract fun classNameEval(value:String?):String
  abstract fun classNameAttr(value: String): String
  abstract fun tabIndexEval(value:Int?):String
  abstract fun tabIndexAttr(value:Int):String
  abstract fun onClickAttr(value:String):String
  abstract fun onClickEval(value:String?):String
  abstract fun colSpanEval(value:Int?):String
}
