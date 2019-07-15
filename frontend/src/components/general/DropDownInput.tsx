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
  tabIndex?: number;
}

class Store {
  @observable
  isOpen: boolean = false;
  @observable
  selectedIndex: number = -1;

  @action
  updateState(isOpen: boolean) {
    this.isOpen = isOpen;
  }

  @action
  incremenSelectedIndex(max:number) {
    this.isOpen = true;
    if(this.selectedIndex<max){
      this.selectedIndex++;
    }
  }
  @action
  decremenSelectedIndex() {
    this.isOpen = true;
    if(this.selectedIndex>0){
      this.selectedIndex--;
    }
  }
}

export function createDefaultDropDownInputStore(): Store {
  return new Store();
}

function resolveValue() {
  if (this.props.current === null) return "";
  else return this.props.element[this.props.current];
}

function toggle(store: Store, disabled: boolean) {
  if (!disabled) {
    store.updateState(!store.isOpen);
  }
}

function selectItem(updater, element, store: Store, item) {
  updater(element[item]);
  store.updateState(false);
}

function onDropDownButtonKeyDown(
  this:DropDownInput,
  size: number
): (KeyboardEvent) => void {
  const store:Store = this.props.store;
  const updater:(value: number) => void = this.props.updateValue;
  const element:{ [index: number]: string } = this.props.element;
  return (event: KeyboardEvent) => {
    switch (event.keyCode) {
      case 38: //up key
        store.decremenSelectedIndex()
        event.preventDefault();
        break;
      case 40: //down key
        store.incremenSelectedIndex(size-1)
        event.preventDefault();
        break;
      case 13:
          selectItem(
            updater,
            element,
            store,
            element[store.selectedIndex]
          )
          event.preventDefault();
          break;

    }
  };
}

function onButtonBlur() {
  this.props.store.updateState(false);
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

    let currentComponent:DropDownInput = this;

    return (
      <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
        <label>{this.props.label}</label>
        <div className="input-group">
          <div className="input-group-prepend">
            <button
              tabIndex={this.props.tabIndex}
              className="btn btn-outline-secondary dropdown-toggle"
              type="button"
              disabled={this.props.disabled}
              onKeyDown={onDropDownButtonKeyDown.bind(currentComponent)(labelsCount)}
              onClick={() => toggle(this.props.store, disabled)}
              onBlur={onButtonBlur.bind(this)}>
              Choose maker...
            </button>
            <div className={classesIsOpen}>
              {labels.map((item,idx) => {
                return (
                  <a
                    key={item}
                    onMouseDown={event => event.preventDefault()}
                    onClick={() =>
                      selectItem(
                        this.props.updateValue,
                        this.props.element,
                        this.props.store,
                        item
                      )
                    }
                    className={classNames({"dropdown-item":true,"selected":this.props.store.selectedIndex===idx})}
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
