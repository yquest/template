import { observer } from "mobx-react";
import * as React from "react";
import { Notifications } from "../app/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { CarEditor } from "../tpl/CarEditorTpl";
import { stores } from "../../stores/Stores";
import { navbar } from "../app/props/NavbarProps";

export const App = observer(() => (
  <div className="container app">
    <Modal></Modal>
    {navbar.createComponent()}
    <Notifications></Notifications>
    <CarList />
    {stores.carEdition.isReadyToEdition && stores.user.authenticated && <CarEditor></CarEditor>}
    {uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}
  </div>
));
