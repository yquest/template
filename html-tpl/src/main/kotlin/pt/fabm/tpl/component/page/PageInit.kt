package pt.fabm.tpl.component.page

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject

interface PageInit {
  val pageInitData: JsonObject
  val auth: Boolean
  val page: Buffer
  fun render()
}
