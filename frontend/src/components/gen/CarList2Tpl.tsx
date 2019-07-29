import { observer } from "mobx-react";
import * as React from "react";
import { CarList2Props } from "../app/props/CarList2Props";
import { CarView2 } from "./CarView2Tpl";
import { MAKERS } from "../../model/Car";
import { dateToStringReadable } from "../../util";

export const CarList2 = observer((props: CarList2Props) => (
  <table>
    <tbody>
      <tr>
        <th>Make</th>
        <th>Model</th>
        <th>Maturity date</th>
        <th>Price</th>
        {props.authenticated && <th colSpan={2}>Actions</th>}
      </tr>
      {props.cars.map((car, idx) => (
        <CarView2
          authenticated={props.authenticated}
          carEdit={() => {
            console.log("hello world");
          }}
          key={idx}
          maker={MAKERS[car.make]}
          maturityDate={dateToStringReadable(car.maturityDate)}
          model={car.model}
          price={car.price + "€"}
          remove={() => {}}
        />
      ))}
    </tbody>
  </table>
));
