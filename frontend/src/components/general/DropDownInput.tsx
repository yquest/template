import * as React from "react";
import classNames from "classnames/bind";
import { observer } from "mobx-react";
import { observable, action } from "mobx";

export interface DropDownInputProps {
  disabled?: boolean;
  name: string;
  error: string;
  current: number;
  label: string;
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

function resolveValue() {
  if (this.props.current === null) return "";
  else return this.props.element[this.props.current];
}

function toggle(store:Store,disabled: boolean) {
  if (!disabled) {
    store.updateState(!store.isOpen);
  }
}

function selectItem(updater,element,store:Store,item) {
  updater(element[item]);
  store.updateState(false);
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

    let disabled = this.props.disabled || false;
    let labelsCount: number = Object.keys(this.props.element).length / 2;
    let labels: string[] = [...Array(labelsCount).keys()].map(
      key => this.props.element[key]
    );

    let currentComponent = this;

    return (
      <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
        <label>{this.props.label}</label>
        <div className="input-group">
          <div className="input-group-prepend">
            <button
              className="btn btn-outline-secondary dropdown-toggle"
              type="button"
              disabled={this.props.disabled}
              onClick={() => toggle(this.props.store,disabled)}
              onBlur={() => this.props.store.updateState(false)}>
              
              Choose maker...
            </button>
            <div className={classesIsOpen}>
              {labels.map(item => {
                return (
                  <a
                    key={item}
                    onMouseDown={event => event.preventDefault()}
                    onClick={() => selectItem(this.props.updateValue,this.props.element,this.props.store,item)}
                    className="dropdown-item"
                    href="javascript:void(0)">
                    {item}
                  </a>
                );
              })}
            </div>
          </div>
          <input
            name={this.props.name}
            type="text"
            className={classesIsValid}
            value={resolveValue.apply(currentComponent)}
            disabled
          />
          {this.props.error !== null && this.props.error.length > 0 && (
            <div className="invalid-feedback">{this.props.error}</div>
          )}
        </div>
      </div>
    );
  }
}
