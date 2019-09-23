import { Car, carFromJson, MAKERS } from "./model/Car";
import * as React from "react";
import { CarManager } from "./components/app/props/CarManager";
import { App } from "./components/gen/AppTpl";
import { app } from "./components/app/props/AppProps";
import { observer } from "mobx-react";
import { Page2 } from "./components/gen/Page2Tpl";
import { observable, configure, action } from "mobx";
import { ModalContent } from "./UIStore";
import { uiStore, showModal } from "./components/tpl/ModalTpl";
import { form1, Control } from "./components/events/Page2Events";

configure({ enforceActions: "observed" });

let router: Router = observable(
  {
    page: window["__state"]["page"],
    updatePage(page) {
      router.page = page;
    }
  },
  {
    updatePage: action
  }
);

const stateHandler = state => {
  const current: Router = state === null ? window["__state"] : state;
  router.updatePage(current.page);
};

interface AppInitialData extends Router {
  cars: {
    make: string;
    model: string;
    maturityDate: number;
    price: number;
  }[];
  username: string;
  edit: boolean;
  auth: boolean;
}

window.addEventListener("popstate", e => stateHandler(e.state));

interface Router {
  page: string;
  updatePage(page: String);
}
const clickLogin = e => {
  e.preventDefault();
};
const clickLogOff = e => {
  form1.updateValue(Control.INPUT1,"bla1")
  form1.updateValue(Control.INPUT2,"bla2")

  const state = { page: "page2" };
  stateHandler(state);
  history.pushState(state, "page 2", "page2");
  e.preventDefault();
};

const carManagerCreator = (car: Car) => {
  const cm: CarManager = {
    car: car,
    edit: e => {
      e.preventDefault();
      console.log(`update car ${MAKERS[car.make]}-${car.model}`);
    },
    remove: e => {
      e.preventDefault();
      console.log(`remove car ${MAKERS[car.make]}-${car.model}`);
      uiStore.modalContent = new ModalContent();
      uiStore.modalContent.title = "Alert";
      uiStore.modalContent.content = (
        <div>
          Tens a certeza que quieres apagar {MAKERS[car.make]}-{car.model}
        </div>
      );
      uiStore.modalContent.actionButton = "Remove";
      uiStore.modalContent.actionEvent = e => {
        console.log(`car removed ${MAKERS[car.make]}-${car.model}`);
      };
      showModal();
    }
  };
  return cm;
};

export const Root = observer(() => {
  const initialData = window["__state"] as AppInitialData;
  if (router.page === "page2") {
    console.log(`render page2 ${router.page}`);
    return (
      <Page2
        username={initialData.username}
        appState={app.AppState.CAR_EDIT_AUTH}
        loginOn={clickLogin}
        loginOff={clickLogOff}
        authenticated={initialData.auth}
      />
    );
  } else {
    return (
      <App
        carManagerCreator={carManagerCreator}
        authenticated={initialData.auth}
        cars={initialData.cars.map(carFromJson)}
        username={initialData.username}
        loginOn={clickLogin}
        loginOff={clickLogOff}
        appState={app.AppState.CAR_EDIT_AUTH}
      />
    );
  }
});