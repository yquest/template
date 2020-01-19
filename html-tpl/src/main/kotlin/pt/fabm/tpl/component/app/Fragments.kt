package pt.fabm.tpl.component.app

import io.vertx.core.buffer.Buffer
import pt.fabm.tpl.component.TagElement

class Fragments(private val buffer: Buffer) {

  fun clientNotifications() {
    TagElement(buffer, "Notifications").createSingleTag()
  }

  fun serverNotifications() {
    buffer.appendString("""<div class="fixed-top container"><div class="float-right"></div></div>""")
  }
}
