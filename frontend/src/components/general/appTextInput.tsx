import * as React from "react";
import { isString } from "util";

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
}

function getInputTypeRef(inputType: InputType) {
  switch (inputType) {
    case InputType.PASSWORD:
      return "password";
    case InputType.TEXT:
      return "text";
    case InputType.DATE_TIME:
      return "text";
    case InputType.NUMBER:
      return "number";
  }
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
    return (
      <div className="col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
        <input
          className={"form-control " + validationClass}
          type={getInputTypeRef(this.props.inputType)}
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
        />
        {this.props.error !== null && this.props.error.length > 0 && (
          <div className="invalid-feedback">{this.props.error}</div>
        )}
      </div>
    );
  }
}
