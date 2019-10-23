import * as React from "react";
import { selectInput } from "../../../controllers/SelectInputController";

export const SelectInput = (props: selectInput.Props) => (
  <select className={props.className} onChange={props.onChange} value={props.selected} tabIndex={props.tabIndex}>
    {props.list.map((value, index) => (
      <option key={props.toKey(index, value)} value={index}>
        {value}
      </option>
    ))}
  </select>
);
