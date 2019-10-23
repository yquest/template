import * as React from "react";
import { carView } from "../app/controllers/CarViewController";

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
        {props.blockedRemove && <i className="btn fas fa-times text-muted"></i>}
        {!props.blockedRemove && (
          <a href="" className="btn" onClick={props.onRemove}>
            <i className="fas fa-times"></i>
          </a>
        )}
      </td>
    )}
  </tr>
);
