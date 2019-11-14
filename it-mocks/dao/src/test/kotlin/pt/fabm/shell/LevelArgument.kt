package pt.fabm.shell

interface LevelArgument {
  val isString:Boolean
  val isList:Boolean
  val level:Int
  fun addRoot(node:String):ValuesLevelArgument = ValuesLevelArgument(node,0)
}
