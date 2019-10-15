import * as React from "react";
import { carView } from "../app/props/CarViewProps";

export const CarView = (props: carView.Props) => (
  <tr className={props.classes}>
    <td>{props.maker}</td>
    <td>{props.model}</td>
    <td>{props.maturityDate}</td>
    <td>{props.price}</td>
    {props.authenticated && (
      <td>
        <a href="" className="btn" onClick={props.onEdit}>
          <i className="fas fa-edit"></i>
        </a>
      </td>
    )}
    {props.authenticated && (
      <td>
        <a href="" className="btn" onClick={props.onRemove}>
          <i className="fas fa-times"></i>
        </a>
      </td>
    )}
  </tr>
);
