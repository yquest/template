import { Car, MAKERS } from "./model/Car";
import * as React from "react";
import { app2 } from "./components/app/props/App2Props";
import { App2 } from "./components/gen/App2Tpl";
import * as ReactDOM from "react-dom";
import { CarManager } from "./components/app/props/CarManager";

const clickLogin = () => {
  console.log("clicked login");
};
const clickLogOff = () => {
  console.log("clicked logoff");
};


const carManager: CarManager = {
  edit: (car: Car) => () => {
      console.log(`update model:${car.model} -> maker:${MAKERS[car.make]}`);
  },
  remove: (car: Car) => () => {
    console.log(`update model:${car.model} -> maker:${MAKERS[car.make]}`);
  }
};

const App = () => {
  let car: Car = {
    make: MAKERS.VOLKSWAGEN,
    maturityDate: new Date(),
    model: "golf v",
    price: 3000,
    getPK: () => {
      return car;
    }
  };

  const cars = [car];
  return (
    <App2
      appState={app2.AppState.LIST_NO_AUTH}
      authenticated={true}
      cars={cars}
      loginOn={clickLogin}
      loginOff={clickLogOff}
      username={"xico"}
      carManager={carManager}
    />
  );
};

ReactDOM.hydrate(<App />, document.getElementById("root"));
