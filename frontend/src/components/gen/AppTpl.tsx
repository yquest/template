import { observer } from "mobx-react";
import { app } from "../app/props/AppProps";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../UIStore";
import { Modal } from "../tpl/ModalTpl"; 

export const App = observer((props: app.Props) => ( <div className="container app"><Modal></Modal>{props.appState === app.AppState.LIST_NO_AUTH && (<a href="" onClick={props.loginOn}>Sign in<i className="fas fa-sign-in-alt"></i></a>)}<Notifications></Notifications>{props.appState === app.AppState.CAR_EDIT_AUTH && (<div className="float-right">{"Hello "+ props.username + " "}<a href="" onClick={props.loginOff}>logoff<i className="fas fa-sign-out-alt"></i></a></div>)}{(props.appState === app.AppState.LIST_NO_AUTH || props.appState === app.AppState.CAR_EDIT_AUTH) && (<CarList cars={props.cars} authenticated={props.authenticated} carManagerCreator={props.carManagerCreator}></CarList>)}{uiStore.modelInDOM && (<div className="modal-backdrop fade show"></div>)}</div>));