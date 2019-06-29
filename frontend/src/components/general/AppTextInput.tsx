import * as React from "react";

export enum InputType {
  TEXT,
  PASSWORD,
  SELECT,
  DATE_TIME,
  NUMBER
}

export interface AppInputProps {
  onChange(value: string): void;
  inputType: InputType;
  label: string;
  labelId: string;
  error: string;
  placeholder?: string;
  disabled?:boolean;
  currentValue: string;
}

function getInputTypeRef(inputType: InputType, value: any) {
  switch (inputType) {
    case InputType.PASSWORD:
      return "password";
    case InputType.TEXT:
      return "text";
    case InputType.DATE_TIME:
      return value === null ? "text" : "date-time";
    case InputType.NUMBER:
      return "number";
  }
}

function resolveName() {
  if (this.props.currentValue === null) {
    return "";
  }
  return this.props.currentValue;
}

export class AppInput extends React.Component<AppInputProps, {}> {
  render() {
    let validationClass;
    if (this.props.error === null) {
      validationClass = "";
    } else if (this.props.error === "") {
      validationClass = "is-valid";
    } else {
      validationClass = "is-invalid";
    }
    let disabled:boolean = this.props.disabled||false;
    return (
      <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
        <label>{this.props.label}</label>
        <input
          disabled={disabled}
          className={"form-control " + validationClass}
          type={getInputTypeRef(this.props.inputType, this.props.currentValue)}
          onChange={event => this.props.onChange(event.target.value)}
          placeholder={this.props.label}
          onFocus={event => {
            if (this.props.inputType === InputType.DATE_TIME) {
              event.target.type = "datetime-local";
            }
          }}
          onBlur={event => {
            if (
              this.props.inputType === InputType.DATE_TIME &&
              event.target.value.length === 0
            ) {
              event.target.type = "text";
            }
          }}
          value={resolveName.bind(this)()}
        />

        {this.props.error !== null && this.props.error.length > 0 && (
          <div className="invalid-feedback">{this.props.error}</div>
        )}
      </div>
    );
  }
}
