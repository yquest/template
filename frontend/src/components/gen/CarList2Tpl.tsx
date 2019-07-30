import { observer } from "mobx-react";
import * as React from "react";
import { CarView2 } from "./CarView2Tpl";
import { MAKERS } from "../../model/Car";
import { dateToStringReadable } from "../../util";
import { carList2 } from "../app/props/CarList2Props";

export const CarList2 = observer((props: carList2.Props) => (
  <table>
    <thead>
      <tr>
        <th>Make</th>
        <th>Model</th>
        <th>Maturity date</th>
        <th>Price</th>
        {props.authenticated && <th colSpan={2}>Actions</th>}
      </tr>
    </thead>
    <tbody>
      {props.cars.map((car, idx) => (
        <CarView2
          authenticated={props.authenticated}
          key={idx}
          maker={MAKERS[car.make]}
          maturityDate={dateToStringReadable(car.maturityDate)}
          model={car.model}
          price={car.price + "€"}
          carManager={props.carManager}
          car={car}
        />
      ))}
    </tbody>
  </table>
));
