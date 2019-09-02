package pt.fabm.tpl.gen

fun main() {
  val tsGens = listOf<TsGen>(
    AppGen(), CarListGen(), CarViewGen()
  )
  TsGen.generateFiles(tsGens)
}
