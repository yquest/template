import { app } from "../app/props/AppProps";
import * as React from "react";
import { navbar } from "../app/props/NavbarProps"

export const Navbar = (props: navbar.Props) => (
  <div className="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom">
    <h5 className="my-0 mr-md-auto font-weight-normal">Company name</h5>
    <nav className="my-2 my-md-0 mr-md-3">
      <a className="p-2 text-dark" href="#">
        Features
      </a>
      <a className="p-2 text-dark" href="#">
        Enterprise
      </a>
      <a className="p-2 text-dark" href="#">
        Support
      </a>
      <a className="p-2 text-dark" href="#">
        Pricing
      </a>
    </nav>
    {props.appState === app.AppState.CAR_EDIT_AUTH && (
      <a className="btn btn-outline-primary" href="" onClick={props.loginOff}>
        Logoff <i className="fas fa-sign-out-alt"></i>
      </a>
    )}
    {props.appState === app.AppState.LIST_NO_AUTH && (
      <a href="" onClick={props.loginOn}>
        Sign in<i className="fas fa-sign-in-alt"></i>
      </a>
    )}
  </div>
);
