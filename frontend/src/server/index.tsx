import express from "express";
import { renderToString } from "react-dom/server";
import * as React from "react";
import { App } from "../components/gen/AppTpl";
import { app } from "../components/app/props/AppProps";
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

const appProps = app.createProps();
const Root = () => {
  return <App createCarClick={appProps.createCarClick}/>;
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