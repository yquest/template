import { observer } from "mobx-react";
import * as React from "react";
import { CarView } from "./CarView";
import { observable, flow, action } from "mobx";
import { Car, MAKERS, CarPK } from "../../model/Car";
import { carService } from "../../services/CarServiceImp";
import { dateToStringReadable } from "../../util";
/*
enum State {
  PENDING,
  DONE,
  ERROR
}

export class CarStore {
  @observable cars: Car[] = [];
  @observable state: State = State.PENDING;
  @observable dropDownOpen: boolean = false;
  detail: Car;

  @action
  removeCar(carPK: CarPK) {
    let idx:number;
    for (let i = 0; i < carStore.cars.length; i++) {
      const found = carStore.cars[i];      
      idx = i;
      if(found.model === carPK.model && found.make === carPK.make) break;
    }
    carStore.cars.splice(idx,1);
  }

  @action
  createCar(car: Car) {
    this.cars.push(car);
  }
  @action
  updateCar(car: Car) {
    let idx: number;
    for (let i = 0; i < this.cars.length; i++) {
      const carInList = this.cars[i];
      idx = i;
      if (car.make == carInList.make && car.model === carInList.model) break;
    }
    this.cars[idx] = car;
  }
}
export interface CarListProps {
  wideWidth: boolean;
  authenticated: boolean;
  updateCar: (car: Car) => void;
  removeCar: (car: CarPK) => void;
}
export const carStore = new CarStore();

@observer
export class CarsList extends React.Component<CarListProps, any> {
  render() {
    let createTable;
    if (this.props.wideWidth) {
      createTable = () => (
        <table>
          <tbody>
            <tr>
              <th>Make</th>
              <th>Model</th>
              <th>Maturity date</th>
              <th>Price</th>
              {this.props.authenticated && <th colSpan={2}>Actions</th>}
            </tr>
            {carStore.cars.map((car, idx) => (
              <CarView
                key={idx}
                car={car}
                authenticated={this.props.authenticated}
                edit={this.props.updateCar}
                remove={this.props.removeCar}
              />
            ))}
          </tbody>
        </table>
      );
    } else {
      createTable = () => (
        <div className="card" style={{ width: "100%" }}>
          {carStore.cars.map((car, idx) => (
            <div key={idx} className="card-body">
              <div className="row bg-light">
                <strong className="col-sm">Make</strong>
                <div className="col-sm">{MAKERS[car.make]}</div>
              </div>
              <div className="row">
                <strong className="col-sm ">Model</strong>
                <div className="col-sm">{car.model}</div>
              </div>
              <div className="row bg-light">
                <strong className="col-sm">Date</strong>
                <div className="col-sm">
                  {dateToStringReadable(car.maturityDate)}
                </div>
              </div>
              <div className="row">
                <strong className="col-sm">Price</strong>
                <div className="col-sm">{car.price}</div>
              </div>
              {this.props.authenticated && (
                <div
                  className="row float-right"
                  style={{ marginRight: "1rem", marginTop: "1rem" }}>
                  <a
                    href="#"
                    className="btn btn-light"
                    onClick={() => this.props.updateCar(car)}
                    style={{ marginRight: "1rem" }}>
                    Edit <i className="fas fa-pen" />
                  </a>
                  <a
                    href="#"
                    className="btn btn-light"
                    onClick={() => this.props.removeCar(car.getPK())}>
                    Remove <i className="fas fa-times" />
                  </a>
                </div>
              )}
            </div>
          ))}
        </div>
      );
    }
    return (
      <div>
        <h3>Car List</h3>
        <div
          className="card"
          style={{ marginTop: "1rem", marginBottom: "1rem" }}>
          <div style={{ padding: "1.5rem" }}>
            {carStore.state == State.PENDING && <div>loading...</div>}
            {carStore.state == State.ERROR && (
              <div>Ups impossible to load data</div>
            )}
            {carStore.state == State.DONE && carStore.cars.length == 0 && (
              <div>No cars...</div>
            )}
            {carStore.state == State.DONE &&
              carStore.cars.length > 0 &&
              createTable()}
          </div>
        </div>
        <div className="d-flex justify-content-end">
          <button
            type="button"
            className="btn btn-primary"
            onClick={() => carStore.fetchCars()}>
            fetch cars
          </button>
        </div>
      </div>
    );
  }
}

carStore.fetchCars();
*/