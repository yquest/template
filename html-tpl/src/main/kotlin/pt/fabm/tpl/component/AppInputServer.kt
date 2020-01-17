package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

class AppInputServer(
  appendable: Buffer,
  private val type: Type,
  private val tabIndex: Int? = null,
  private val placeHolder: String? = null,
  private val disabled: Boolean? = null,
  private val value: String
) : AppInput(appendable), MultiEnvTemplateServer {
  override fun input() {
    val input = TagElement(buffer, "input")
      .startStarterTag()
    //attributes
    appendServer(""" class="form-control"""")
    appendServer(""" tabindex="$tabIndex"""")
    if (type != Type.TEXT)
      appendServer(""" type="${type.label.toLowerCase()}"""")
    if (disabled == true) appendServer(""" disabled="disabled"""")
    if (placeHolder != null) appendServer(""" placeholder="$placeHolder"""")
    appendServer(""" value="$value"""")
    input.endStarterTag().endTag()

  }
}
