import * as React from "react";
import { app } from "../app/props/AppProps";
import {
  getErrorClass,
  getInputTypeRef,
  onFocus,
  onBlur
} from "../events/AppInputEvents";

export const AppInput = (props: app.TextInputProps) => (
  <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
    <label>{props.label}</label>
    <input
      tabIndex={props.tabIndex}
      disabled={props.disabled || false}
      className={getErrorClass(props.error)}
      type={getInputTypeRef(props.inputType, props.currentValue)}
      onChange={event => props.onChange(event.target.value)}
      placeholder={props.placeholder}
      onFocus={onFocus(props.inputType)}
      onBlur={onBlur(props.inputType)}
      value={props.currentValue === null ? "" : props.currentValue}
    ></input>
    {props.error !== null && props.error.length > 0 && (
      <div className="invalid-feedback">{props.error}</div>
    )}
  </div>
);
