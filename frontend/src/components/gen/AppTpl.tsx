import { observer } from "mobx-react";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { CarEditor } from "../tpl/CarEditorTpl";
import { stores } from "../../stores/Stores";
import { navbar } from "../app/props/NavbarProps";
import { app } from "../app/props/AppProps";

export const App = observer((props:app.Props) => (
  <div className="container app">
    <Modal></Modal>
    {navbar.createComponent()}
    <Notifications></Notifications>
    <CarList />
    <button className="btn btn-primary form-group" onClick={props.createCarClick} tabIndex={1}>Create car</button>
    {stores.carEdition.isReadyToEdition && stores.user.authenticated && <CarEditor></CarEditor>}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
