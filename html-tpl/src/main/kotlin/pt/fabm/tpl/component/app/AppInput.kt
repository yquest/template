package pt.fabm.tpl.component.app

import pt.fabm.tpl.*

class AppInput(
  tabIndex: Int? = null,
  label: String = "",
  disabled: Boolean = false,
  typeInput: String = "text",
  placeholder: String = "",
  value: Pair<() -> String, String> = { "" } to "",
  onChange: String = "",
  error: String = "",
  type: Type
) : Component(name = "AppInput", type = type, attributes = {
  """ error={$error}""" +
    """ tabIndex={$tabIndex} currentValue={${value.second}}""" +
    """ disabled={$disabled} inputType={a.InputType.TEXT}""" +
    """ label="$label" labelId="$label"""" +
    """ placeholder="$placeholder" onChange={$onChange}"""
}) {
  init {
    val clientImplementationPrefix = """
    import * as React from "react";
    import { app } from "../app/props/AppProps";
    import { getErrorClass, getInputTypeRef, onFocus, onBlur } from "../events/AppInputEvents"
    
    export const AppInput = (props: app.TextInputProps) => (""".trimIndent()
    val clientImplementationSuffix = """
    );
    """.trimIndent()
    val inputServerAttributes = {
      """ tabindex="$tabIndex" """ +
        if (disabled) """disabled="disabled" """ else "" +
          """class="form-control" """ +
          """type="$typeInput" """ +
          """placeholder="$placeholder" """ +
          """value="${value.first()}""""
    }
    val inputClientAttributes = {
      """ tabIndex={props.tabIndex} 
      disabled={props.disabled || false} 
      className={getErrorClass(props.error)}
      type={getInputTypeRef(props.inputType, props.currentValue)}
      onChange={event => props.onChange(event.target.value)}
      placeholder={props.placeholder}
      onFocus={onFocus(props.inputType)}
      onBlur={onBlur(props.inputType)}
      value={props.currentValue === null ? "": props.currentValue}
      """.trimMargin()
    }

    val root = DIV(type, attributes = {
      val classes = "form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3";
      if (type == Type.SERVER) """ class="$classes" """
      else """ className="$classes" """
    })
    root.children += TagElement(
      "label",
      listOf(if (type == Type.SERVER) TextElement(label) else TextElement("{props.label}"))
    ) { "" }.toElementCreator(type)
    root.children += TagElement(
      "input",
      listOf(),
      if (type === Type.SERVER) inputServerAttributes else inputClientAttributes
    ).toElementCreator(type)

    if (type === Type.CLIENT_IMPLEMENTATION)
      root.children +=
        TextElement(
          """
          {props.error !== null && props.error.length > 0 && (
            <div className="invalid-feedback">{props.error}</div>
          )}""".trimIndent()
        ).toElementCreator(type)
    if (type == Type.CLIENT_IMPLEMENTATION)
      children += TextElement(clientImplementationPrefix).toElementCreator(type)
    if (type != Type.CLIENT)
      children += root
    if (type == Type.CLIENT_IMPLEMENTATION)
      children += TextElement(clientImplementationSuffix).toElementCreator(type)
  }
}
