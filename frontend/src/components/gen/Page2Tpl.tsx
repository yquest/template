import { observer } from "mobx-react";
import { page as app } from "../app/props/PageProps";
import { app as a } from "../app/props/AppProps";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { uiStore } from "../../UIStore";
import { Modal } from "../tpl/ModalTpl"; 
import { AppInput } from "./AppInputTpl";
import { form1, submitFormEvent, updateValue, Control } from "../events/Page2Events";

export const Page2 = observer((props: app.Props) => ( <div className="container app"><Modal></Modal>{props.appState === app.AppState.LIST_NO_AUTH && (<a href="" onClick={props.loginOn}>Sign in<i className="fas fa-sign-in-alt"></i></a>)}<Notifications></Notifications>{props.appState === app.AppState.CAR_EDIT_AUTH && (<div className="float-right">{"Hello "+ props.username + " "}<a href="" onClick={props.loginOff}>logoff<i className="fas fa-sign-out-alt"></i></a></div>)}{(props.appState === app.AppState.LIST_NO_AUTH || props.appState === app.AppState.CAR_EDIT_AUTH) && (<div><div>2nd page</div><form onSubmit={submitFormEvent}><AppInput></AppInput><AppInput></AppInput><button className="btn btn-primary" tabIndex={3} >submit</button></form></div>)}{uiStore.modelInDOM && (<div className="modal-backdrop fade show"></div>)}</div>));