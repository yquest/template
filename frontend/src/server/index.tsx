import express from "express";
import { renderToString } from "react-dom/server";
import * as React from "react";
import { Car } from "../model/Car";
import { App } from "../components/gen/AppTpl";
import { CarService } from "../components/app/props/CarService";
import { cars } from "./cars";
const port = 3000;
const server = express();

server.use(express.static("dist"));

const html = ({ body }: { body: string }) => `
<!DOCTYPE html>
<html>
  <head>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link href="bundle.js" rel="stylesheet"/>
  </head>
  <body style="margin:0">
    <div id="root">${body}</div>
    <script type="text/javascript" src="bundle.js"></script>
  </body>
</html>
`;

const createCarManager = (car: Car) => {
  const cm: CarService = {
    car: car,
    edit: () => { },
    remove: () => { }
  }
  return cm;
};

const Root = () => {
  return <App/>;
};

server.get("/", (req, res) => {
  const body = renderToString(React.createElement(Root));

  res.send(
    html({
      body
    })
  );
});

server.listen(3000, () => console.log("Example app listening on port 3000!"));
