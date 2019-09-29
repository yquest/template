import { app } from "../app/props/AppProps";
import * as React from "react";
import { navbar } from "../app/props/NavbarProps"

export const Navbar = (props: navbar.Props) => (
<div><div className="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom"><h5 className="my-0 mr-md-auto font-weight-normal pointer-cursor" onClick={props.pageActions.gotoRoot}>Company name</h5><a href="" className="p-2 text-dark" onClick={props.pageActions.gotoPage2}>Page 2</a>{props.appState === app.AppState.CAR_EDIT_AUTH && (<a href="" className="btn btn-outline-primary" onClick={props.loginOff}>Sign off <i className="fas fa-sign-out-alt"></i></a>)}{props.appState === app.AppState.LIST_NO_AUTH && (<a href="" onClick={props.loginOn}>Sign in <i className="fas fa-sign-in-alt"></i></a>)}</div></div>);