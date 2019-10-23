import { observer } from "mobx-react";
import { loginPage } from "../app/controllers/LoginController";
import * as React from "react";
import { Notifications } from "../tpl/Notifications";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { AppInput } from "./AppInputTpl";
import { Navbar } from "./NavbarTpl";
import { navbar } from "../app/controllers/NavbarController";

export const Login = observer((props: loginPage.Props) => {
  return (
    <div className="container app">
      <Modal></Modal>
      {navbar.createComponent()}
      <Notifications></Notifications>
      <div>
        <div className="row justify-content-sm-center">Login</div>
        <div className="row justify-content-sm-center">
          <div className="card col-sm-6 col-lg-4">
            <div className="card-body">
              <form onSubmit={props.submitForm}>
                {React.createElement(AppInput, { ...props.login })}
                {React.createElement(AppInput, { ...props.password })}
                <a href="" onClick={props.onClickRegisterLink}>
                  Create user
                </a>
                <button className="btn btn-primary col-sm-12" tabIndex={3}>
                  Login
                </button>
              </form>
              {props.showErrorForm && (
                <div className={props.errorFormClasses}>{props.errorForm}</div>
              )}
            </div>
          </div>
        </div>
      </div>
      {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
    </div>
  );
});
