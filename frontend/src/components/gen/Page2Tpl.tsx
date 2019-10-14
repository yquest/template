import { observer } from "mobx-react";
import { page2 } from "../app/props/Page2Props";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { AppInput } from "./AppInputTpl";
import { Navbar } from "../gen/NavbarTpl";
import { DropDownInput } from "./DropDownTpl";
import { stores } from "../../stores/Stores";

export const Page2 = observer((props: page2.Props) => {
  return (
    <div className="container app">
      <Modal></Modal>
      <Navbar></Navbar>
      {!stores.user.authenticated && (
        <a href="" onClick={stores.navigation.loginPage}>
          Sign in<i className="fas fa-sign-in-alt"></i>
        </a>
      )}
      <Notifications></Notifications>
      {stores.user.authenticated && <div className="float-right"></div>}
      <div>
        <div>2nd page</div>
        <form onSubmit={props.submitForm}>
          {React.createElement(AppInput, { ...props.input1 })}
          {React.createElement(AppInput, { ...props.input2 })}
          {React.createElement(DropDownInput, { ...props.dd1 })}
          <button className="btn btn-primary" tabIndex={3}>
            submit
          </button>
        </form>
      </div>
      {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
    </div>
  );
});
