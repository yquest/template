import { Car, MAKERS } from "./model/Car";
import * as React from "react";
import { app2 } from "./components/app/props/App2Props";
import { App2 } from "./components/gen/App2Tpl";
import * as ReactDOM from "react-dom";
import { CarManager } from "./components/app/props/CarManager";
import { cars } from "./server/cars";

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

const App = () => {
  return (
    <App2
      appState={app2.AppState.LIST_NO_AUTH}
      authenticated={true}
      cars={cars}
      loginOn={clickLogin}
      loginOff={clickLogOff}
      username={"xico"}
      carManagerCreator={carManagerCreator}
    />
  );
};

//ReactDOM.render(<App />,document.getElementById('root'));
ReactDOM.hydrate(<App />, document.getElementById("root"));
