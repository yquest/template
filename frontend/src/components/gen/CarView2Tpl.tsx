import * as React from "react";
import { observer } from "mobx-react";
import { CarView2Props } from "../app/props/CarView2Props.d";

export const CarView2 = observer((props: CarView2Props) => (
  <tr>
    <td>{props.maker}</td>
    <td>{props.model}</td>
    <td>{props.maturityDate}</td>
    <td>{props.price}</td>
    {props.authenticated && (
      <td>
        <a href="javascript:void(0)" className="btn" onClick={props.carEdit}>
          <i className="fas fa-pen" />
        </a>
      </td>
    )}
    {props.authenticated && (
      <td>
        <a href="javascript:void(0)" className="btn" onClick={props.remove}>
          <i className="fas fa-times" />
        </a>
      </td>
    )}
  </tr>
));
