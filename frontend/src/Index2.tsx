import { Car, carToJson, carFromJson } from "./model/Car";
import * as React from "react";
import * as ReactDOM from "react-dom";
import { CarManager } from "./components/app/props/CarManager";
import { CarList } from "./components/gen/CarListTpl";
import { App } from "./components/gen/AppTpl";
import { app } from "./components/app/props/AppProps";

const clickLogin = () => {
  console.log("clicked login");
};
const clickLogOff = () => {
  console.log("clicked logoff");
};

const carManagerCreator = (car: Car) => {
  const cm: CarManager = {
    car: car,
    edit: () => {
      console.log("update car");
    },
    remove: () => {
      console.log("remove car");
    }
  };
  return cm;
};
interface AppInitialData {
  cars: {
    make: string;
    model: string;
    maturityDate: number;
    price: number;
  }[];
  username: string;
  edit: boolean;
  auth: boolean;
}

const initialData = window["appInitialData"] as AppInitialData;

const Root = () => {
  return (
    <App
      carManagerCreator={carManagerCreator}
      authenticated={initialData.auth}
      cars={initialData.cars.map(carFromJson)}
      username={initialData.username}
      loginOn={clickLogin}
      loginOff={clickLogOff}
      appState={app.AppState.CAR_EDIT_AUTH}
    />
  );
  //return (<CarList authenticated={false} carManagerCreator={carManagerCreator} cars={initialData.cars.map(carFromJson)}/>);
};
//ReactDOM.render(<Root />,document.getElementById('root'));
ReactDOM.hydrate(<Root />, document.getElementById("root"));
