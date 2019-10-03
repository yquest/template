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
import { Navbar } from "../gen/NavbarTpl";
import { DropDownInput } from "./DropDownTpl";
import { dropDown } from "../app/props/DropDownProps";
import classNames from "classnames/bind";
import { observable, action } from "mobx";

const dropDownStore = observable(
  {
    open: false,
    selectedIndex: null as number,
    toggle() {
        dropDownStore.open = !dropDownStore.open;
      },
    updateOpen(open:boolean) {
      dropDownStore.open = open;
    },
      updateSelectIndex(idx: number) {
      dropDownStore.selectedIndex = idx;
    }
  },
  {
    toggle: action,
    updateSelectIndex: action,
    updateOpen:action
  }
);

let testProps: dropDown.Props = {
  blur(e) {},
  label: "my label",
  tabIndex: 2,
  get classesIsOpen() {
    return classNames(
      { "dropdown-menu": true },
      { show: dropDownStore.open }
    );
  },
  get classesIsValid() {
    return classNames(
      { "form-control": true },
      { "is-invalid": testProps.error != null && testProps.error.length > 0 },
      { "is-valid": testProps.error != null && testProps.error.length === 0 }
    );
  },
  disabled: false,
  error: null,
  inputName: "myName",
  get inputValue() {
      return dropDownStore.selectedIndex === null ? "" : testProps.labels[dropDownStore.selectedIndex];
  },
  itemClasses(idx) {
    return classNames({
      "dropdown-item": true,
      selected: dropDownStore.selectedIndex === idx
    });
  },
  keyDown(e) {},
  labels: ["lab1", "lab2"],
  togle: dropDownStore.toggle,
  onSelectItem(idx) {
    return e => {
      dropDownStore.updateSelectIndex(idx);
      dropDownStore.updateOpen(false);
      e.preventDefault();
    };
  }
};

export const Page2 = observer((props: app.Props) => (
  <div className="container app">
    <Modal></Modal>
    <Navbar
      appState={props.appState}
      loginOff={props.loginOff}
      loginOn={props.loginOn}
      pageActions={props.pageActions}></Navbar>
    {props.appState === app.AppState.LIST_NO_AUTH && (
      <a href="" onClick={props.loginOn}>
        Sign in<i className="fas fa-sign-in-alt"></i>
      </a>
    )}
    <Notifications></Notifications>
    {props.appState === app.AppState.CAR_EDIT_AUTH && (
      <div className="float-right"></div>
    )}
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
          onChange={updateValue(Control.INPUT1)}></AppInput>
        <AppInput
          error={form1.error[Control.INPUT2]}
          tabIndex={2}
          currentValue={form1.input[Control.INPUT2]}
          disabled={false}
          inputType={a.InputType.TEXT}
          label="my label 2"
          labelId="my label 2"
          placeholder="put something also here"
          onChange={updateValue(Control.INPUT2)}></AppInput>
        <DropDownInput
          inputName={testProps.inputName}
          blur={testProps.blur}
          classesIsOpen={testProps.classesIsOpen}
          classesIsValid={testProps.classesIsValid}
          disabled={testProps.disabled}
          error={testProps.error}
          inputValue={testProps.inputValue}
          itemClasses={testProps.itemClasses}
          onSelectItem={testProps.onSelectItem}
          keyDown={testProps.keyDown}
          tabIndex={testProps.tabIndex}
          label={testProps.label}
          togle={testProps.togle}
          labels={testProps.labels}
        />
        <button className="btn btn-primary" tabIndex={3}>
          submit
        </button>
      </form>
    </div>
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
