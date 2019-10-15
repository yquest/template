import * as React from "react";
import { appInput } from "../app/props/AppInputProps";

export const AppInput = (props: appInput.Props) => (
  <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
    <label>{props.label}</label>
    <input
      tabIndex={props.tabIndex}
      disabled={props.disabled || false}
      className={props.errorClasses}
      type={appInput.Type[props.inputType]}
      onChange={props.onChange}
      placeholder={props.placeholder}
      onFocus={props.onFocus}
      onBlur={props.onBlur}
      value={props.value === null ? "" : props.value}></input>
    {props.error !== null && props.error.length > 0 && (
      <div className="invalid-feedback">{props.error}</div>
    )}
  </div>
);
