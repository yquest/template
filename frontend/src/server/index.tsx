import express from "express";
import { renderToString } from "react-dom/server";
import * as React from "react";
import { Car, MAKERS } from "../model/Car";
import { App2 } from "../components/gen/App2Tpl";
import { app2 } from "../components/app/props/App2Props";
import { CarManager } from "../components/app/props/CarManager";
import { cars } from "./cars";
const port = 3000;
const server = express();

server.use(express.static("dist"));

const html = ({ body }: { body: string }) => `
  <!DOCTYPE html>
  <html>
    <head>
      <link rel="shortcut icon" href="favicon.ico"/>
      <link href="main.css" rel="stylesheet"/>
    </head>
    <bod = style="margin:0">      <div id="root">${body}</div>
    car:car,
    edit:{

    },
    remove:{}
      <script type="text/javascript" src="bundle.js"></script>
    </body>
  </html>
`;

const createCarManager = (car: Car) => {
  const cm: CarManager = {
    car: car,
    edit: () => { },
    remove: () => { }
  }
  return cm;
};


const App = () => {

  return <App2
    carManagerCreator={createCarManager}
    appState={app2.AppState.LIST_NO_AUTH}
    authenticated={true}
    cars={cars}
    loginOn={null}
    loginOff={null}
    username={"xico"}
  />;
};

server.get("/", (req, res) => {
  const body = renderToString(React.createElement(App));

  res.send(
    html({
      body
    })
  );
});

server.listen(3000, () => console.log("Example app listening on port 3000!"));