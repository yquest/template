import { observer } from "mobx-react";
import * as React from "react";
import { Notifications } from "../tpl/Notifications";
import { CarList } from "./CarListTpl";
import { uiStore } from "../../stores/UIStore";
import { Modal } from "../tpl/ModalTpl";
import { CarEditor } from "./CarEditorTpl";
import { stores } from "../../stores/Stores";
import { navbar } from "../app/controllers/NavbarController";
import { app } from "../app/controllers/AppController";

export const App = observer((props: app.Props) => (<div className="container app"><Modal></Modal>{navbar.createComponent()}<Notifications></Notifications><CarList></CarList>{stores.user.authenticated && (<button className="btn btn-primary form-group" tabIndex={1} onClick={props.createCarClick}>Create car</button>)}{stores.carEdition.isReadyToEdition && stores.user.authenticated && (<CarEditor></CarEditor>)}{uiStore.modelInDOM && <div className="modal-backdrop fade show"></div>}</div>));