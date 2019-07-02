import { observable, configure as configureMbox, computed, action } from "mobx";
import * as React from "react";
import { carService } from "./services/CarService";
import { CarsList, carStore } from "./components/app/CarList";
import { CarEditor, updateEditorCar } from "./components/app/CarEditor";
import { uiStore, ModalState, ModalContent } from "./UIStore";
import { LoginEditor } from "./components/app/LoginEditor";
import { observer } from "mobx-react";
import { UserRegisterEditor } from "./components/app/UserRegisterEditor";
import { Car, CarPK, MAKERS } from "./model/Car";
import { Notifications } from "./components/app/Notifications";
import {
  createModalContainer,
  createModalOverlay
} from "./components/app/Modal";
import { userService } from "./services/UserService";

configureMbox({ enforceActions: "observed" }); // don't allow state modifications outside actions

interface AppStateValues {
  wideSpace: boolean;
  selected: number;
  register: boolean;
  login: boolean;
  userName: string;
}

enum AppState {
  LIST_NO_AUTH,
  CAR_EDIT_AUTH,
  LOG_IN_NO_AUTH,
  REGISTER_NO_AUTH
}

class AppStateStore {
  @observable
  appStateValues: AppStateValues = {
    wideSpace: true,
    selected: 0,
    login: false,
    register: false,
    userName: localStorage.getItem("username"),
  };

@computed 
get authenticated():boolean {
  return !(this.appStateValues.userName === "" ||
  this.appStateValues.userName === null ||
  this.appStateValues.userName === undefined)
}

  @computed
  get state(): AppState {
    if (!this.authenticated) {
      if (this.appStateValues.register) {
        return AppState.REGISTER_NO_AUTH;
      } else if (this.appStateValues.login) {
        return AppState.LOG_IN_NO_AUTH;
      }
      return AppState.LIST_NO_AUTH;
    }
    return AppState.CAR_EDIT_AUTH;
  }

  @action
  updateShowRegister(showRegister: boolean) {
    this.appStateValues.register = showRegister;
  }
  @action
  updateLogin(login: boolean) {
    this.appStateValues.login = login;
  }
  @action
  updateUserName(userName: string) {
    this.appStateValues.userName = userName;
  }
  @action
  updateSelected(selected: number) {
    this.appStateValues.selected = selected;
  }
  @action
  updateWideSpace(wideSpace: boolean) {
    this.appStateValues.wideSpace = wideSpace;
  }
}

let appStateStore = new AppStateStore();
function resizewindow() {
  appStateStore.updateWideSpace(window.innerWidth > 1024);
}
resizewindow();
window.addEventListener("resize", resizewindow);

window.addEventListener("popstate", e => {
  switch (window.location.hash) {
    case "#register":
      appStateStore.updateShowRegister(true);
      appStateStore.updateLogin(false);
      break;
    case "#login":
      appStateStore.updateShowRegister(false);
      appStateStore.updateLogin(true);
      break;
    default:
      appStateStore.updateShowRegister(false);
      appStateStore.updateLogin(false);
      break;
  }
});

function onListRemoveCar(carPK: CarPK) {
  uiStore.updateModalAction(()=>{
    carService.removeCar(carPK);
  });
  let modalContent = new ModalContent();
  modalContent.content = `Do you really want to remove car with model:${carPK.model} from maker:${MAKERS[carPK.make]}?`;
  modalContent.title = "Remove car";
  modalContent.actionButton = "Remove"; 
  uiStore.updateModalContent(modalContent);
  uiStore.updateModal(ModalState.CREATED);
}
function onListUpdateCar(car: Car) {
  updateEditorCar(car);
}

@observer
export class App extends React.Component<any, any> {
  render() {
    let container = (
      <div className="container" style={{ marginBottom: "5rem" }}>

        {createModalContainer()}
        {appStateStore.state === AppState.LIST_NO_AUTH && (
          <a
            href="#login"
            onClick={() => {
              appStateStore.updateLogin(true);
            }}>
            login
          </a>
        )}
        <Notifications />
        {appStateStore.state === AppState.CAR_EDIT_AUTH && (
          [<div key="helloUsername">Hello {appStateStore.appStateValues.userName}</div>,<button
          key="btnLogout"
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            userService.userLogout();
            localStorage.removeItem("username");
            localStorage.removeItem(appStateStore.appStateValues.userName);
            appStateStore.updateUserName(null);
          }}>
          logout
        </button>]
        )}
        {(appStateStore.state === AppState.LIST_NO_AUTH ||
          appStateStore.state === AppState.CAR_EDIT_AUTH) && (
          <CarsList
            wideWidth={appStateStore.appStateValues.wideSpace}
            updateCar={onListUpdateCar}
            removeCar={onListRemoveCar}
            authenticated={appStateStore.authenticated}
          />
        )}
        {appStateStore.state === AppState.CAR_EDIT_AUTH && (
          <CarEditor
            saveCarEvent={car => {
              carService.createCar(car)
              .then(res => {
                if (res.status == 204) {
                  let notification = uiStore.createNotification();
                  notification.content = <div>Successefuly created</div>;
                  uiStore.addNotificationTemp(notification, 3000);
                  carStore.createCar(car);
                }
              });
            }}
          />
        )}
        {appStateStore.state === AppState.LOG_IN_NO_AUTH && (
          <LoginEditor
            loginSuccessefull={user => {
              appStateStore.updateUserName(user);
              localStorage.setItem("username", user);
            }}
            showUserRegister={() => {
              appStateStore.updateShowRegister(true);
              window.location.hash = "#register";
            }}
          />
        )}
        {appStateStore.state === AppState.REGISTER_NO_AUTH && (
          <UserRegisterEditor
            returnToLoginClick={() => {
              appStateStore.updateShowRegister(false);
              window.location.hash = "#login";
            }}
            successefullyRegistered={() => {
              appStateStore.updateShowRegister(false);
              appStateStore.updateLogin(false);
            }}
          />
        )}
        {createModalOverlay()}
      </div>
    );
    return container;
  }
}