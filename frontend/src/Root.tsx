import { observer } from "mobx-react";
import { loginPage } from "./components/app/controllers/LoginController";
import { stores } from "./stores/Stores";
import { App } from "./components/gen/AppTpl";
import * as React from "react";
import { registerPage } from "./components/app/controllers/RegisterUserController";
import { app } from "./components/app/controllers/AppController";

console.log("ssr");

const appProps = app.createProps();
export const Root = observer(() => {
  if (stores.router.page === "login") {
    return loginPage.createComponent();
  } else if (stores.router.page === "register") {
    return registerPage.createComponent();
  } else {
    return <App createCarClick={appProps.createCarClick} />;
  }
});
