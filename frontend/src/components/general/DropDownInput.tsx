import * as React from "react";
import classNames from "classnames/bind";
import { observer } from "mobx-react";

export interface DropDownInputProps {
  current: number
  element: { [index: number]: string };
  updateValue: (value: number) => void;
  store:Store;
}

export interface Store{
  isOpen:boolean;
  updateState: (isOpen:boolean) => void;
}

@observer
export class DropDownInput extends React.Component<DropDownInputProps,any> {
  render(){
    var classes = classNames(
      { "dropdown-menu": true },
      { show: this.props.store.isOpen }
    );
  
    let labelsCount: number = Object.keys(this.props.element).length / 2;
    let labels: string[] = [...Array(labelsCount).keys()].map(
      key => this.props.element[key]
    );
  
  
    return <div className="col-6">
      <div className="input-group mb-3">
        <div className="input-group-prepend">
          <button
            className="btn btn-outline-secondary dropdown-toggle"
            type="button"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"
            onClick={() => {this.props.store.updateState(!this.props.store.isOpen)}}
            onBlur={(event) => {this.props.store.updateState(false)}}
          >Choose maker...</button>
          <div className={classes}>
            {labels.map(label => {
              return (
                <a
                  key={label}
                  onMouseDown={(event) => event.preventDefault()}
                  onClick={(event) => {
                    this.props.updateValue(this.props.element[label]);
                    this.props.store.updateState(false);
                  }}
                  className="dropdown-item"
                  href="#"
                >
                  {label}
                </a>
              );
            })}
          </div>
        </div>
        <input
          type="text"
          className="form-control"
          aria-label="Text input with dropdown button"
          value={this.props.current == null ? "" : this.props.element[this.props.current]}
          disabled
        />
      </div>
    </div>
  }
}