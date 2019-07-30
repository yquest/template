import express from "express";
import { renderToString } from "react-dom/server";
import * as React from "react";
import { Car, MAKERS } from "../model/Car";
import { App2 } from "../components/gen/App2Tpl";
import { app2 } from "../components/app/props/App2Props";
import { CarManager } from "../components/app/props/CarManager";
const port = 3000;
const server = express();

server.use(express.static("dist"));

const html = ({ body }: { body: string }) => `
  <!DOCTYPE html>
  <html>
    <head>
      <link rel="shortcut icon" href="favicon.ico"><link href="main.css?2085304dda2273394a86" rel="stylesheet"></head>
    </head>
    <body style="margin:0">
      <div id="root">${body}</div>
      <script type="text/javascript" src="bundle.js?eacfda388806c316a4fe"></script>
    </body>
  </html>
`;

const carManager: CarManager = {
  edit: (car: Car) => () => {
      console.log("update ignored");
  },
  remove: (car: Car) => () => {
    console.log("remove ignored");
  }
};


const App = () => {
  let car: Car = {
    make: MAKERS.VOLKSWAGEN,
    maturityDate: new Date(),
    model: "golf v",
    price: 3000,
    getPK: () => {
      return car;
    }
  };

  const cars = [car];
  return <App2
    carManager={carManager}
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