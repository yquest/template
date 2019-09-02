package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import java.io.Closeable

interface TsGen:Closeable {
  companion object{
    fun generateFiles(gens:List<TsGen>){
      for(gen in gens){
        val element= gen.creator
        val appendable = gen.appendable
        element.renderTag(appendable)
        gen.close()
      }
    }
  }
  val appendable:Appendable
  val creator:Element
}
