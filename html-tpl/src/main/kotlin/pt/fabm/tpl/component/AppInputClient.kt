package pt.fabm.tpl.component

import io.vertx.core.buffer.Buffer

class AppInputClient(buffer: Buffer) : AppInput(buffer), MultiEnvTemplateClient, ClientElement{

  override fun input(){
    val input = TagElement(buffer, "input")
      .startStarterTag()
    //attributes
    appendClient(" tabIndex={props.tabIndex} disabled={props.disabled || false} className={props.errorClasses}")
    appendClient(" type={resolveType(props.inputType)}")
    appendClient(" onChange={props.onChange} ")
    appendClient(" placeholder={props.placeholder} onFocus={props.onFocus} onBlur={props.onBlur}")
    appendClient(" value={props.value === null ? \"\" : props.value}")
    input.endStarterTag().endTag()
  }

  override fun renderImplementation() {
    appendBody("""
      import * as React from "react";
      import { appInput } from "../../../controllers/AppInputController";

      function resolveType(type:appInput.Type):string{
        if(type == appInput.Type.TEXT)
        return null
        else
        return appInput.Type[type].toLowerCase()
      }

      export const AppInput = (props: appInput.Props) => (
        <div className="form-group">
          <label>{props.label}</label>
    """.trimIndent())
    input()
    appendBody(
      """{props.error !== null && props.error.length > 0 && (
          <div className="invalid-feedback">{props.error}</div>
        )}
       </div>
      );
    """.trimIndent())
  }

}
