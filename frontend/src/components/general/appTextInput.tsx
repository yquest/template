import * as React from "react";
import { isString } from "util";

export enum InputType {
  TEXT,
  PASSWORD,
  SELECT
}

export interface AppInputProps {
  onChange(value: string): void;
  inputType: InputType;
  label: string;
  labelId: string;
  error: string;
}

function getInputTypeRef(inputType: InputType) {
  switch (inputType) {
    case InputType.PASSWORD:
      return "password";
    case InputType.TEXT:
      return "text";
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
      <div>
        <div className="form-group row">
          <label
            htmlFor={this.props.labelId}
            className="col-2 col-form-label is-invalid">
            {this.props.label}
          </label>
        </div>
        <div className="col-6">
          <input
            className={"form-control " + validationClass}
            type={getInputTypeRef(this.props.inputType)}
            onChange={event => this.props.onChange(event.target.value)}
          />
          {this.props.error !== null && this.props.error.length > 0 && (
            <div className="invalid-feedback">{this.props.error}</div>
          )}
        </div>
      </div>
    );
  }
}
