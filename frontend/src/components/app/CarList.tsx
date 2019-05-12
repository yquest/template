import { observer } from "mobx-react";
import * as React from "react";
import { CarView } from "./CarView";
import { observable, flow, action } from "mobx";
import { Car } from "../../model/Car";
import { carService } from "../../services/CarService";

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

  fetchCars = flow(function*() {
    this.state = State.PENDING;
    try {
      const cars = yield carService.fetchCars();
      this.state = State.DONE;
      this.cars = cars;
    } catch (error) {
      this.state = State.ERROR;
      console.error(error);
    }
  });

  @action
  createCar(car:Car){
    this.cars.push(car);
  }
}

export const carStore = new CarStore();

@observer
export class CarsList extends React.Component<{}, any> {
  render() {
    return (
      <div>
        <h3>Car List</h3>
        <div
          className="card"
          style={{ marginTop: "1rem", marginBottom: "1rem" }}
        >
          <div style={{ padding: "1.5rem" }}>
            {carStore.state == State.PENDING && <div>loading...</div>}
            {carStore.state == State.ERROR && (
              <div>Ups impossible to load data</div>
            )}
            {carStore.state == State.DONE && carStore.cars.length == 0 && (
              <div>No cars...</div>
            )}
            {carStore.state == State.DONE && carStore.cars.length > 0 && (
              <table>
                <tbody>
                  <tr>
                    <th>Make</th>
                    <th>Model</th>
                    <th>Maturity date</th>
                    <th>Price</th>
                  </tr>
                  {carStore.cars.map((car, idx) => (
                    <CarView key={idx} car={car} />
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
        <button
          type="button"
          className="btn btn-primary"
          onClick={() => carStore.fetchCars()}
        >
          fetch cars
        </button>
      </div>
    );
  }
}

carStore.fetchCars();
