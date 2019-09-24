package pt.fabm.tpl.gen

import pt.fabm.tpl.Element
import java.io.Closeable
import java.io.FileWriter

interface TsGen : Closeable {
  companion object {
    fun generateFiles(gens: List<TsGen>) {
      for (gen in gens) {
        val element = gen.creator
        val appendable = gen.appendable
        element.renderTag(appendable)
        gen.close()
      }
    }

    fun fromElement(file: String, elementGetter: () -> Element): TsGen {
      val writer = FileWriter(file)
      return object : TsGen {
        override val appendable: Appendable = writer
        override val creator: Element = elementGetter()
        override fun close() {
          writer.close()
        }
      }
    }
  }

  val appendable: Appendable
  val creator: Element
}
