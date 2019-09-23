import { observer } from "mobx-react";
import { app } from "../app/props/AppProps";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../UIStore";
import { Modal } from "../tpl/ModalTpl";
import { Navbar } from "../tpl/NavBar";

export const App = observer((props: app.Props) => (
  <div>
    <Navbar appState={props.appState} loginOff={props.loginOff} loginOn={props.loginOn}></Navbar>
    <div className="container app">
      <Modal></Modal>
      <div>
        {props.appState === app.AppState.LIST_NO_AUTH && (
          <a href="" onClick={props.loginOn}>
            Sign in<i className="fas fa-sign-in-alt"></i>
          </a>
        )}
        <Notifications></Notifications>
      </div>
      {(props.appState === app.AppState.LIST_NO_AUTH ||
        props.appState === app.AppState.CAR_EDIT_AUTH) && (
        <CarList
          cars={props.cars}
          authenticated={props.authenticated}
          carManagerCreator={props.carManagerCreator}></CarList>
      )}
      {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
    </div>
  </div>
));
