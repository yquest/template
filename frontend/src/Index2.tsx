import { Car, carToJson } from "./model/Car";
import * as React from "react";
import { app } from "./components/app/props/AppProps";
import { App } from "./components/gen/AppTpl";
import * as ReactDOM from "react-dom";
import { CarManager } from "./components/app/props/CarManager";
import { cars } from "./server/cars";
import { CarList } from "./components/gen/CarListTpl";

const clickLogin = () => {
  console.log("clicked login");
};
const clickLogOff = () => {
  console.log("clicked logoff");
};

const carManagerCreator = (car:Car)=>{
  const cm:CarManager = {
    car: car,
    edit:()=>{
      console.log("update car");
    },
    remove:()=>{
      console.log("remove car");
    }
  }
  return cm;
}

const Root = () => {
  return (<CarList authenticated={false} carManagerCreator={carManagerCreator} cars={cars}/>);
};
//ReactDOM.render(<Root />,document.getElementById('root'));
ReactDOM.hydrate(<Root />, document.getElementById("root"));
console.log(carToJson(cars[0]));