import * as React from "react";
import * as ReactDOM from "react-dom";
import { App } from "./components/gen/AppTpl";

ReactDOM.render(
  <App createCarClick={() => {
    console.log("click");
  }} />,
  document.getElementById("root")
);
