import { Root } from "./Root";
import * as ReactDOM from "react-dom";
import * as React from "react";
import { stores } from "./stores/Stores";

if(window.location.pathname === '/login') stores.router.update('login');
else if(window.location.pathname === '/register') stores.router.update('register');
ReactDOM.render(<Root />, document.getElementById("root"));