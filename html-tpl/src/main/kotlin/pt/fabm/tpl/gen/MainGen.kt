package pt.fabm.tpl.gen

fun main() {
  TsGen.generateFiles(
    listOf(
      AppGen(), CarListGen(), CarViewGen()
    )
  )
}
