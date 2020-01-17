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
    <label>{props.label}</label><input tabIndex={props.tabIndex} disabled={props.disabled || false} className={props.errorClasses} type={resolveType(props.inputType)} onChange={props.onChange}  placeholder={props.placeholder} onFocus={props.onFocus} onBlur={props.onBlur} value={props.value === null ? "" : props.value}></input>{props.error !== null && props.error.length > 0 && (
          <div className="invalid-feedback">{props.error}</div>
        )}
       </div>
      );