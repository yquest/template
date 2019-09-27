import { observer } from "mobx-react";
import { app } from "../app/props/AppProps";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../UIStore";
import { Modal } from "../tpl/ModalTpl";
import { Navbar } from "../gen/NavbarTpl";
import { CarEditor } from "../gen/CarEditorTpl";


export const App = observer((props: app.Props) => (
  <div className="container app">
    <Modal></Modal>
    <Navbar
      appState={props.appState}
      loginOff={props.loginOff}
      loginOn={props.loginOn}
      pageActions={props.pageActions}
    ></Navbar>
    {props.appState === app.AppState.LIST_NO_AUTH && (
      <a href="" onClick={props.loginOn}>
        Sign in<i className="fas fa-sign-in-alt"></i>
      </a>
    )}
    <Notifications></Notifications>
    {props.appState === app.AppState.CAR_EDIT_AUTH && (
      <div className="float-right"></div>
    )}
    {(props.appState === app.AppState.LIST_NO_AUTH ||
      props.appState === app.AppState.CAR_EDIT_AUTH) && (
      <CarList
        cars={props.cars}
        authenticated={props.authenticated}
        carManagerCreator={props.carManagerCreator}
      ></CarList>
    )}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
    {(props.carEditProps !== null &&(<CarEditor title="duno" maturityDate={props.carEditProps.maturityDate} onSubmit={props.carEditProps.onSubmit}/>))}
  </div>
));
