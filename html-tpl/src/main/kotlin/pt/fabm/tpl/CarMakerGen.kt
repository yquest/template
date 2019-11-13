package pt.fabm.tpl

import io.vertx.core.buffer.Buffer
import pt.fabm.template.models.type.CarMake
import pt.fabm.tpl.component.ClientElement

class CarMakerGen(private val buffer:Buffer) : ClientElement{
  override fun renderImplementation() {
    val enumName = "CarMaker"

    buffer.appendString("enum $enumName{")
    val iterator = CarMake.values().iterator()
    buffer.appendString(iterator.next().name)
    iterator.forEachRemaining { buffer.appendString(", ${it.name}") }
    buffer.appendString("}")
    buffer.appendString("namespace $enumName{")
    buffer.appendString("export function getLabel(instance:$enumName):String{")
    buffer.appendString("switch(instance){")
    CarMake.values().forEach { buffer.appendString("""case $enumName.${it.name}: return "${it.label}";""") }
    buffer.appendString("}}}")
  }
}
