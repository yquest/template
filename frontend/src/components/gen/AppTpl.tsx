import { observer } from "mobx-react";
import * as React from "react";
import { uiStore } from "../../UIStore";
import { Notifications } from "../app/Notifications";
import { app } from "../app/props/AppProps";
import { Modal } from "../tpl/ModalTpl";
import { CarList } from "./CarListTpl";
import { Navbar } from "../gen/NavbarTpl";

export const App = observer((props: app.Props) => (
  <div className="container app">
    <Navbar
      loginOff={props.loginOff}
      loginOn={props.loginOn}
      pageActions={props.pageActions}
      appState={app.AppState.CAR_EDIT_AUTH}
    />
    <Modal></Modal>
    {props.appState === app.AppState.LIST_NO_AUTH && (
      <a href="" onClick={props.loginOn}>
        Sign in<i className="fas fa-sign-in-alt"></i>
      </a>
    )}
    <Notifications></Notifications>
    {(props.appState === app.AppState.LIST_NO_AUTH ||
      props.appState === app.AppState.CAR_EDIT_AUTH) && (
      <CarList
        cars={props.cars}
        authenticated={props.authenticated}
        carManagerCreator={props.carManagerCreator}
      ></CarList>
    )}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
