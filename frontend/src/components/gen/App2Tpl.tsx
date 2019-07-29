import { observer } from "mobx-react";
import { app2 } from "../app/props/App2Props";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList2 } from "./CarList2Tpl";

export const App2 = observer((props: app2.Props) => (
  <div className="container app">
    {props.appState === app2.AppState.LIST_NO_AUTH && (
      <a className="float-right" href="#login" onClick={props.loginOn}>
        Sign in <i className="fas fa-sign-in-alt" />
      </a>
    )}
    <Notifications />
    {props.appState === app2.AppState.CAR_EDIT_AUTH && (
      <div key="helloUsername" className="float-right">
        Hello {props.username + " "}
        <a href="javascript:void(0)" onClick={props.loginOff}>
          log off <i className="fas fa-sign-out-alt" />
        </a>
      </div>
    )}
    {(props.appState === app2.AppState.LIST_NO_AUTH ||
      props.appState === app2.AppState.CAR_EDIT_AUTH) && (
      <CarList2 carManager={props.carManager} cars={props.cars} authenticated={props.authenticated}  />
    )}
  </div>
));
