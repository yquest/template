import * as React from "react";
import * as ReactDOM from "react-dom";
import { App } from "./components/gen/AppTpl";
import { app } from "./components/app/controllers/AppController";

ReactDOM.render(
  <App createCarClick={app.createProps} />,
  document.getElementById("root")
);
