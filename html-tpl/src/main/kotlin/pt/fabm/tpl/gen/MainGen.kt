package pt.fabm.tpl.gen

import pt.fabm.tpl.component.ClientElement
import pt.fabm.tpl.component.app.*
import java.io.FileWriter

fun main() {
  val map = mapOf<String, (Appendable) -> ClientElement>(
    "CarViewTpl.tsx" to { a -> CarViewClient(a) },
    "AppTpl.tsx" to { a -> AppClient(a) },
    "RegisterTpl.tsx" to { a -> RegisterUserClient(a) },
    "CarListTpl.tsx" to { a -> CarListClient(a) },
    "NavbarTpl.tsx" to { a -> NavBarClient(a) },
    "LoginTpl.tsx" to { a -> LoginClient(a) }
  )
  map.entries.forEach { entry ->
    val fw = FileWriter(entry.key)
    entry.value(fw).renderImplementation()
    fw.close()
  }
}
