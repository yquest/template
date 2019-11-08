package pt.fabm.tpl.component

class AppInputServer(
  appendable: Appendable,
  private val type: Type,
  private val tabIndex: Int? = null,
  private val placeHolder: String? = null,
  private val disabled: Boolean? = null,
  private val value: String
) : AppInput(appendable), MultiEnvTemplateServer {
  override fun input() {
    val input = TagElement(appendable, "input")
      .startStarterTag()
    //attributes
    appendServer(""" tabindex="$tabIndex"""")
    appendServer(""" type="${type.label}"""")
    if (disabled == true) appendServer(""" disabled="disabled"""")
    appendClient(" tabIndex={props.tabIndex} disabled={props.disabled || false} className={props.errorClasses}")
    appendClient(" type={appInput.Type[props.inputType]} onChange={props.onChange} ")
    appendClient(" placeholder={props.placeholder} onFocus={props.onFocus} onBlur={props.onBlur}")
    appendClient(" value={props.value === null ? \"\" : props.value}")
    if (placeHolder != null) appendServer(""" placeholder="$placeHolder"""")
    appendServer(""" value="$value"""")
    input.endStarterTag().endTag()

  }
}
