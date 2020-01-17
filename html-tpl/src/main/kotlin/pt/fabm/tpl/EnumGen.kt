package pt.fabm.tpl

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement

class EnumGen<T : Enum<T>>(
  private val buffer: Buffer,
  private val name: String,
  private val values: Array<T>,
  private val labelRes: ((e: T) -> String)?
) : ClientElement {
  private fun renderLabelResolver(lr: (e: T) -> String) {
    buffer.appendString(
      """
      export function labels():Array<string>{
        return new Array(size).map(idx=>getLabel(idx));
      }""".trimMargin()
    )
    buffer.appendString("export function getLabel(instance:e):string{")
    buffer.appendString("switch(instance){")
    values.forEach { buffer.appendString("""case e.${it.name}: return "${lr(it)}";""") }
    buffer.appendString("}}")
  }

  override fun renderImplementation() {
    buffer.appendString("export namespace $name{")
    buffer.appendString("enum e{")
    val size = values.size
    val iterator = values.iterator()
    buffer.appendString(iterator.next().name)
    iterator.forEachRemaining { buffer.appendString(", ${it.name}") }
    buffer.appendString("}")
    buffer.appendString("export const size = $size;")
    if (labelRes != null)
      renderLabelResolver(labelRes)
    buffer.appendString("}")
  }
}
