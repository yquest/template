package pt.fabm.tpl.gen

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.CarMakerGen
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.app.*
import java.io.File
import java.nio.file.Paths

fun main() {
  val vertx = Vertx.vertx()
  val components = "components/gen"
  val models = "model/gen"

  val map = mapOf<String, (Buffer) -> ClientElement>(
    "$components/CarViewTpl.tsx" to { buffer -> CarViewClient(buffer) },
    "$components/AppTpl.tsx" to { buffer -> AppClient(buffer) },
    "$components/RegisterTpl.tsx" to { buffer -> RegisterUserClient(buffer) },
    "$components/CarListTpl.tsx" to { buffer -> CarListClient(buffer) },
    "$components/NavbarTpl.tsx" to { buffer -> NavBarClient(buffer) },
    "$components/LoginTpl.tsx" to { buffer -> LoginClient(buffer) },
    "$models/CarMaker.ts" to { buffer -> CarMakerGen(buffer) }
  )

  fun makeParentIfNotExists(dir: File) {
    val parent = dir.parentFile ?: File(".")
    if (!parent.exists())
      makeParentIfNotExists(parent)
    if (!dir.exists())
      dir.mkdir()
  }

  map.entries.forEach { entry ->
    val buffer = Buffer.buffer()
    entry.value(buffer).renderImplementation()
    makeParentIfNotExists(Paths.get(entry.key).parent.toFile())
    vertx.fileSystem().writeFile(entry.key, buffer) {
      if (it.failed()) it.cause().printStackTrace()
      else println("write in file ${entry.key}")
    }
  }
  vertx.close()
}
