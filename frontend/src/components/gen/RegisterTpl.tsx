import { observer } from "mobx-react";
import * as React from "react";
import { Notifications } from "../tpl/Notifications";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { AppInput } from "./AppInputTpl";
import { registerPage } from "../app/controllers/RegisterUserController";
import { navbar } from "../app/controllers/NavbarController";

export const Register = observer((props: registerPage.Props) => ( <div className="container app"><Modal</Modal>{navbar.createComponent()}<Notifications</Notifications><div><div className="row justify-content-sm-center">User Register</div><div className="row justify-content-sm-center"><div className="card col-sm-6 col-lg-4"><div className="card-body"><form onSubmit={props.submitForm}>{React.createElement(AppInput, { ...props.username })}{React.createElement(AppInput, { ...props.password })}{React.createElement(AppInput, { ...props.email })}<button className="btn btn-primary col-sm-12" tabIndex={4}>Register User</button></form></div></div></div></div>{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}</div>));