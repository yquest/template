import { observer } from "mobx-react";
import { loginPage } from "./components/app/props/LoginProps";
import { stores } from "./stores/Stores";
import { App } from "./components/gen/AppTpl";
import * as React from "react";
import { registerPage } from "./components/app/props/RegisterUserProps";
import { app } from "./components/app/props/AppProps";

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
