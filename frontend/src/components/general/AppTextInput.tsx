import * as React from "react";
import classNames from "classnames/bind";

/*
function getInputTypeRef(inputType: app.InputType, value: any) {
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

function resolveName() {
  if (this.props.currentValue === null) {
    return "";
  }
  return this.props.currentValue;
}

function getErrorClass(error: String) {
  return classNames({
    "form-control": true,
    "is-valid": error === "",
    "is-invalid": error !== ""
  });
}

function onFocus(
  inputType: app.InputType
): (event: React.FocusEvent<HTMLInputElement>) => void {
  return event => {
    if (inputType === app.InputType.DATE_TIME) {
      event.target.type = "datetime-local";
    }
  };
}

function onBlur(
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

export const AppInput = (props: app.TextInputProps) => (
  <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
    <label>{props.label}</label>
    <input
      tabIndex={props.tabIndex}
      disabled={props.disabled || false}
      className={getErrorClass(props.error)}
      type={getInputTypeRef(props.inputType, props.currentValue)}
      onChange={event => props.onChange(event.target.value)}
      placeholder={props.label}
      onFocus={onFocus(props.inputType)}
      onBlur={onBlur(props.inputType)}
      value={resolveName.bind(this)()}
    />
    {props.error !== null && props.error.length > 0 && (
      <div className="invalid-feedback">{props.error}</div>
    )}
  </div>
);
*/