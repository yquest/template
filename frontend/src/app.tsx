import { observable, configure as configureMbox, computed, action } from "mobx";
import * as React from "react";
import { carService } from "./services/CarService";
import { CarsList, carStore } from "./components/app/CarList";
import { CarEditor } from "./components/app/CarEditor";
import {
  Notifications,
  notificationStore
} from "./components/app/Notifications";
import { LoginEditor } from "./components/app/LoginEditor";
import { observer } from "mobx-react";
import { UserRegisterEditor } from "./components/app/UserRegisterEditor";

configureMbox({ enforceActions: "observed" }); // don't allow state modifications outside actions

interface AppStateValues {
  selected:number;
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
    selected : 0,
    login: false,
    register: false,
    userName: localStorage.getItem("username")
  };

  @computed
  get state(): AppState {
    if (
      this.appStateValues.userName === "" ||
      this.appStateValues.userName === null ||
      this.appStateValues.userName === undefined
    ) {
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
}

let appStateStore = new AppStateStore();

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

@observer
export class App extends React.Component<{}, {}> {
  render() {
    console.log("render " + appStateStore.state);
    return (
      <div className="container" style={{ marginBottom: "5rem" }}>
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
          <div>Hello {appStateStore.appStateValues.userName}</div>
        )}
        {(appStateStore.state === AppState.LIST_NO_AUTH ||
          appStateStore.state === AppState.CAR_EDIT_AUTH) && <CarsList />}
        {appStateStore.state === AppState.CAR_EDIT_AUTH && (
          <CarEditor
            saveCarEvent={car => {
              carService.createCar(car).then(res => {
                if (res.status == 204) {
                  let notification = notificationStore.createNotification();
                  notification.content = <div>Successefuly created</div>;
                  notificationStore.addNotificationTemp(notification, 3000);
                  carStore.createCar(car);
                }
              });
            }}
            logoutEvent={() => {
              localStorage.removeItem(appStateStore.appStateValues.userName);
              appStateStore.updateUserName(null);
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
      </div>
    );
  }
}
