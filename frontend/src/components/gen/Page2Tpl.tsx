import { observer } from "mobx-react";
import { page as app } from "../app/props/PageProps";
import { app as a } from "../app/props/AppProps";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { uiStore } from "../../UIStore";
import { Modal } from "../tpl/ModalTpl";
import { AppInput } from "./AppInputTpl";
import {
  form1,
  submitFormEvent,
  updateValue,
  Control
} from "../events/Page2Events";
import { Navbar } from "./NavbarTpl";

export const Page2 = observer((props: app.Props) => (
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
    {props.appState === app.AppState.CAR_EDIT_AUTH && (
      <div className="float-right">
        {"Hello " + props.username + " "}
        <a href="" onClick={props.loginOff}>
          logoff<i className="fas fa-sign-out-alt"></i>
        </a>
      </div>
    )}
    {(props.appState === app.AppState.LIST_NO_AUTH ||
      props.appState === app.AppState.CAR_EDIT_AUTH) && (
      <div>
        <div>2nd page</div>
        <form onSubmit={submitFormEvent}>
          <AppInput
            error={form1.error[Control.INPUT1]}
            tabIndex={1}
            currentValue={form1.input[Control.INPUT1]}
            disabled={false}
            inputType={a.InputType.TEXT}
            label="my label"
            labelId="my label"
            placeholder="put something here"
            onChange={updateValue(Control.INPUT1)}
          ></AppInput>
          <AppInput
            error={form1.error[Control.INPUT2]}
            tabIndex={2}
            currentValue={form1.input[Control.INPUT2]}
            disabled={false}
            inputType={a.InputType.TEXT}
            label="my label 2"
            labelId="my label 2"
            placeholder="put something also here"
            onChange={updateValue(Control.INPUT2)}
          ></AppInput>
          <button className="btn btn-primary" tabIndex={3}>
            submit
          </button>
        </form>
      </div>
    )}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
