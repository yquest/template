package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import pt.fabm.tpl.component.page.PageInit

class AppServerNoSSR(
  override val auth: Boolean,
  override val pageInitData: JsonObject, override val page: Buffer
) : PageInit {
  override fun render() {
    //do nothing
  }
}
