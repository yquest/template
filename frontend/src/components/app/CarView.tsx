import { MAKERS, Car } from "../../model/Car";
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

@observer
export class CarView extends React.Component<{ car: Car }, any> {
  render() {
    const car = this.props.car;
    return (
      <tr>
        <td>{MAKERS[car.make]}</td>
        <td>{car.model}</td>
        <td>{dateToString(car.maturityDate)}</td>
        <td>{car.price}</td>
      </tr>
    );
  }
}
