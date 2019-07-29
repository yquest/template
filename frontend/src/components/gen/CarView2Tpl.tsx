import * as React from "react";
import { carView2 } from "../app/props/CarView2Props";

export const CarView2 = (props: carView2.Props) => (
  <tr>
    <td>{props.maker}</td>
    <td>{props.model}</td>
    <td>{props.maturityDate}</td>
    <td>{props.price}</td>
    {props.authenticated && (
      <td>
        <a href="javascript:void(0)" className="btn" onClick={props.carManager.edit(props.car)}>
          <i className="fas fa-pen" />
        </a>
      </td>
    )}
    {props.authenticated && (
      <td>
        <a href="javascript:void(0)" className="btn" onClick={props.carManager.remove(props.car)}>
          <i className="fas fa-times" />
        </a>
      </td>
    )}
  </tr>
);