package pt.fabm.tpl.gen

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.app.*
import java.io.FileWriter

fun main() {
  val vertx= Vertx.vertx()
  val map = mapOf<String, (Buffer) -> ClientElement>(
    "CarViewTpl.tsx" to { a -> CarViewClient(a) },
    "AppTpl.tsx" to { a -> AppClient(a) },
    "RegisterTpl.tsx" to { a -> RegisterUserClient(a) },
    "CarListTpl.tsx" to { a -> CarListClient(a) },
    "NavbarTpl.tsx" to { a -> NavBarClient(a) },
    "LoginTpl.tsx" to { a -> LoginClient(a) }
  )
  map.entries.forEach { entry ->
    val buffer = Buffer.buffer();
    entry.value(buffer).renderImplementation()
    vertx.fileSystem().writeFile(entry.key,buffer){
      println("write in file ${entry.key}")
    }
  }
}
