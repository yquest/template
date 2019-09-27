import * as React from "react";

interface SelectInputProps {
  className?: string;
  tabIndex?:number;
  list: string[];
  onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  toKey: (index: number, value: string) => string;
}

export const SelectInput = (props: SelectInputProps) => (
  <select className={props.className} onChange={props.onChange}>
    {props.list.map((value, index) => (
      <option key={props.toKey(index, value)} value={index}>
        {value}
      </option>
    ))}
  </select>
);
