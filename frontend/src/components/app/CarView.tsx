import { MAKERS, Car, CarPK } from "../../model/Car";
import * as React from "react";
import { observer } from "mobx-react";
import { pad, dateToStringReadable } from "../../util";

export interface CarViewProps {
  car: Car;
  edit: (car: Car) => void;
  remove: (carPK: CarPK) => void;
  authenticated: boolean;
  key:any
}

@observer
export class CarView extends React.Component<CarViewProps, any> {
  render() {
    const car = this.props.car;
    return (
      <tr>
        <td>{MAKERS[car.make]}</td>
        <td>{car.model}</td>
        <td>{dateToStringReadable(car.maturityDate)}</td>
        <td>{car.price}</td>
        {this.props.authenticated && [
          <td key={1}>
            <a
              href="javascript:void(0)"
              className="btn"
              onClick={() => this.props.edit(car)}>
              <i className="fas fa-pen" />
            </a>
          </td>,
          <td key={2}>
            <a
              href="javascript:void(0)"
              className="btn"
              onClick={() => this.props.remove(car.getPK())}>
              <i className="fas fa-times" />
            </a>
          </td>
        ]}
      </tr>
    );
  }
}
