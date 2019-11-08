package pt.fabm.tpl.component

class AppInputClient(appendable: Appendable) : AppInput(appendable), MultiEnvTemplateClient{

  override fun input(){
    val input = TagElement(appendable, "input")
      .startStarterTag()
    //attributes
    appendClient(" tabIndex={props.tabIndex} disabled={props.disabled || false} className={props.errorClasses}")
    appendClient(" type={appInput.Type[props.inputType]} onChange={props.onChange} ")
    appendClient(" placeholder={props.placeholder} onFocus={props.onFocus} onBlur={props.onBlur}")
    appendClient(" value={props.value === null ? \"\" : props.value}")
    input.endStarterTag().endTag()
  }

}
