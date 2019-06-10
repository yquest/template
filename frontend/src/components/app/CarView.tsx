import { MAKERS, Car, CarPK } from "../../model/Car";
import * as React from "react";
import { observer } from "mobx-react";
import { pad } from "../../util";

function dateToString(date: Date): string {
  return `${date.getFullYear()}-${pad(date.getMonth(), 2)}-${pad(
    date.getDay(),
    2
  )}, ${pad(date.getHours(), 2)}:${pad(date.getMinutes(), 2)}:${pad(
    date.getSeconds(),
    2
  )}`;
}

export interface CarViewProps{
  car: Car;
  edit:(car:Car)=>void;
  remove:(carPK:CarPK)=>void;
}

@observer
export class CarView extends React.Component<CarViewProps, any> {
  render() {
    const car = this.props.car;
    return (
      <tr>
        <td>{MAKERS[car.make]}</td>
        <td>{car.model}</td>
        <td>{dateToString(car.maturityDate)}</td>
        <td>{car.price}</td>
        <td>
          <a href="javascript:void(0)" className="btn" onClick={()=>this.props.edit(car)}>
            <i className="fas fa-pen" />
          </a>
        </td>
        <td>
          <a href="javascript:void(0)" className="btn" onClick={()=>this.props.remove(car.getPK())}>
          <i className="fas fa-times"></i>
          </a>
        </td>
      </tr>
    );
  }
}
