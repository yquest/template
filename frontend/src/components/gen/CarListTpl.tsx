import * as React from "react";
import { carList } from "../app/props/CarListProps";
import { MAKERS } from "../../model/Car";
import { CarView } from "./CarViewTpl";
import { dateToStringReadable } from "../../util";
const noContent = () => <div>no cars available</div>;
const content = (props: carList.Props) => (
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
        <CarView
          authenticated={props.authenticated}
          key={idx}
          maker={MAKERS[car.make]}
          model={car.model}
          maturityDate={dateToStringReadable(car.maturityDate)}
          price={car.price + "€"}
          carManager={props.carManagerCreator(car)}
          car={car}
        ></CarView>
      ))}
    </tbody>
  </table>
);
export const CarList = (props: carList.Props) =>
  props.cars.length === 0 ? noContent() : content(props);
