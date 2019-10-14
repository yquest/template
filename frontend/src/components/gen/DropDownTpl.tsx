import { observer } from "mobx-react";
import { dropDown } from "../app/props/DropDownProps";
import * as React from "react";

export const DropDownInput = observer((props: dropDown.Props) => {
  return (
  <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3" onKeyDown={props.keyDown}>
    <label>{props.label}</label>
    <div className="input-group">
      <div className="input-group-prepend">
        <button
          tabIndex={props.tabIndex}
          className="btn btn-outline-secondary dropdown-toggle"
          type="button"
          disabled={props.disabled}
          onClick={props.togle}
          onBlur={props.blur}>
          Choose maker...
        </button>
        <div className={props.classesIsOpen}>
          {props.labels.map((item,idx) => {
            return (
              <a
                key={item}
                onMouseDown={event => event.preventDefault()}
                onClick={props.onSelectItem(idx)}
                className={props.itemClasses}
                href="">
                {item}
              </a>
            );
          })}
        </div>
      </div>
      <input
        name={props.inputName}
        type="text"
        className={props.classesIsValid}
        value={props.inputValue}
        disabled
      />
      {props.error !== null && props.error.length > 0 && (
        <div className="invalid-feedback">{props.error}</div>
      )}
    </div>
  </div>
);});
