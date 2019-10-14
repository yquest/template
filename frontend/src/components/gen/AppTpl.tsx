import { observer } from "mobx-react";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { Navbar } from "../gen/NavbarTpl";
import { CarEditor } from "../tpl/CarEditorTpl";
import { stores } from "../../stores/Stores";

export const App = observer(() => (
  <div className="container app">
    <Modal></Modal>
    <Navbar></Navbar>
    <Notifications></Notifications>
    {stores.carEdition.isReadyToEdition && <div className="float-right"></div>}
    <CarList />
    {stores.carEdition.isReadyToEdition && <CarEditor></CarEditor>}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
