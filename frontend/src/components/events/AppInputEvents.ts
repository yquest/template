import { app } from "../app/props/AppProps";
import classNames from "classnames/bind";

export function getInputTypeRef(inputType: app.InputType, value: any) {
    switch (inputType) {
      case app.InputType.PASSWORD:
        return "password";
      case app.InputType.TEXT:
        return "text";
      case app.InputType.DATE_TIME:
        return value === null ? "text" : "date-time";
      case app.InputType.NUMBER:
        return "number";
    }
  }
  
  export function getErrorClass(error: String) {
    return classNames({
      "form-control": true,
      "is-valid": error === "",
      "is-invalid": (error || "").length > 0
    });
  }
  
  export function onFocus(
    inputType: app.InputType
  ): (event: React.FocusEvent<HTMLInputElement>) => void {
    return event => {
      if (inputType === app.InputType.DATE_TIME) {
        event.target.type = "datetime-local";
      }
    };
  }
  
  export function onBlur(
    inputType: app.InputType
  ): (event: React.FocusEvent<HTMLInputElement>) => void {
    return event => {
      if (
        inputType === app.InputType.DATE_TIME &&
        event.target.value.length === 0
      ) {
        event.target.type = "text";
      }
    };
  }
  