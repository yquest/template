import * as React from "react";
import { appInput } from "../../controllers/AppInputController";

export const AppInput = (props: appInput.Props) => (
  <div className="form-group">
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
