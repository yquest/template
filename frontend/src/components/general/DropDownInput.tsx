import * as React from "react";
import classNames from "classnames/bind";
import { observer } from "mobx-react";
import { observable, action } from "mobx";

export interface DropDownInputProps {
  error: string;
  current: number;
  element: { [index: number]: string };
  updateValue: (value: number) => void;
  store: Store;
}

class Store {
  @observable
  isOpen: boolean = false;
  @action
  updateState(isOpen: boolean) {
    this.isOpen = isOpen;
  }
}

export function createDefaultDropDownInputStore(): Store {
  return new Store();
}

@observer
export class DropDownInput extends React.Component<DropDownInputProps, any> {
  render() {
    var classesIsOpen = classNames(
      { "dropdown-menu": true },
      { show: this.props.store.isOpen }
    );

    var classesIsValid = classNames(
      { "form-control": true },
      { "is-invalid": this.props.error != null && this.props.error.length > 0 },
      { "is-valid": this.props.error != null && this.props.error.length === 0 }
    );

    let labelsCount: number = Object.keys(this.props.element).length / 2;
    let labels: string[] = [...Array(labelsCount).keys()].map(
      key => this.props.element[key]
    );

    return (
        <div className="input-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
          <div className="input-group-prepend">
            <button
              className="btn btn-outline-secondary dropdown-toggle"
              type="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
              onClick={() => {
                this.props.store.updateState(!this.props.store.isOpen);
              }}
              onBlur={() => {
                this.props.store.updateState(false);
              }}>
              Choose maker...
            </button>
            <div className={classesIsOpen}>
              {labels.map(label => {
                return (
                  <a
                    key={label}
                    onMouseDown={event => event.preventDefault()}
                    onClick={() => {
                      this.props.updateValue(this.props.element[label]);
                      this.props.store.updateState(false);
                    }}
                    className="dropdown-item"
                    href="#">
                    {label}
                  </a>
                );
              })}
            </div>
          </div>
          <input
            type="text"
            className={classesIsValid}
            aria-label="Text input with dropdown button"
            value={
              this.props.current == null
                ? ""
                : this.props.element[this.props.current]
            }
            disabled
          />
          {this.props.error !== null && this.props.error.length > 0 && (
            <div className="invalid-feedback">{this.props.error}</div>
          )}
        </div>
    );
  }
}
