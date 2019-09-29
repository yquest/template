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
import { PageActions } from "./components/app/props/PageActions";
import { form1, Control } from "./components/events/Page2Events";
import { carEdit } from "./components/app/props/CarEditorProps";

configure({ enforceActions: "observed" });
const initialData = window["__state"] as AppInitialData;
app.carStore.init(initialData.cars.map(carFromJson));

if ("serviceWorker" in navigator) {
  navigator.serviceWorker
    .register("/sw.js", { scope: "/" })
    .then(function(registration) {
      console.log("Service Worker Registered");
    });
  navigator.serviceWorker.ready.then(function(registration) {
    console.log("Service Worker Ready");
  });
}

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

const pageActions: PageActions = {
  gotoPage2(e) {
    form1.updateValue(Control.INPUT1, "bla1");
    form1.updateValue(Control.INPUT2, "bla2");
    const state = { page: "page2" };
    stateHandler(state);
    history.pushState(state, "page 2", "/page2");
    e.preventDefault();
  },
  gotoRoot(e) {
    const state = { page: "" };
    stateHandler(state);
    history.pushState(state, "", "/");
    e.preventDefault();
  }
};

const clickLogOff = e => {
  console.log("loggoff");
  e.preventDefault();
};

const carEditProps: carEdit.Props = {
  maturityDate: {
    calendarIconClasses: "fa fa-calendar-day",
    onCalendarChange(e) {
      console.log(`change calendar`)
    },
    onChangeYear(e) {
      const nDate = new Date(carEdit.carStore.maturityDate);
      nDate.setFullYear(e.target.value);
      carEdit.carStore.updateMaturityDate(nDate)
    },
    onChangeMonth(e) {
      const nDate = new Date(carEdit.carStore.maturityDate);
      nDate.setMonth(e.target.value);
      carEdit.carStore.updateMaturityDate(nDate)
    },
    onChangeHour(e) {
      const nDate = new Date(carEdit.carStore.maturityDate);
      nDate.setHours(e.target.value);
      carEdit.carStore.updateMaturityDate(nDate)
    },
    onChangeDay(e) {
      const nDate = new Date(carEdit.carStore.maturityDate);
      nDate.setDate(e.target.value);
      carEdit.carStore.updateMaturityDate(nDate)
    },
    onChangeMinutes(e) {
      const nDate = new Date(carEdit.carStore.maturityDate);
      nDate.setMinutes(e.target.value);
      carEdit.carStore.updateMaturityDate(nDate)
    },
    onClickShowCalendar(e) {
      console.log(`change calendar`)
      uiStore.toggleCarEditCalendar();
    },
    get openedCalendar() {
      return uiStore.carEditCalendarShow;
    },
    get value() {
      return app.carStore.selectedCar === null
        ? null
        : carEdit.carStore.maturityDate;
    }
  },
  onSubmit(e) {},
  title: "edit"
};

const carManagerCreator = (car: Car) => {
  const cm: CarManager = {
    car: car,
    edit: e => {
      e.preventDefault();
      app.carStore.updateSelected(car);
      carEdit.carStore.updateMaturityDate(car.maturityDate);
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
      uiStore.modalContent.actionEvent = _ => {
        app.carStore.remove(car);
      };
      showModal();
    }
  };
  return cm;
};

export const Root = observer(() => {
  if (router.page === "page2") {
    console.log(`render page2 ${router.page}`);
    return (
      <Page2
        username={initialData.username}
        appState={app.AppState.CAR_EDIT_AUTH}
        loginOn={clickLogin}
        loginOff={clickLogOff}
        authenticated={initialData.auth}
        pageActions={pageActions}
      />
    );
  } else {
    return (
      <App
        carEditProps={carEditProps}
        carManagerCreator={carManagerCreator}
        authenticated={initialData.auth}
        cars={app.carStore.cars}
        username={initialData.username}
        loginOn={clickLogin}
        loginOff={clickLogOff}
        appState={app.AppState.CAR_EDIT_AUTH}
        pageActions={pageActions}
      />
    );
  }
});
