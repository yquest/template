import * as React from "react";
import { stores } from "../../stores/Stores";
import { observer } from "mobx-react";
import { navbar } from "../app/controllers/NavbarController";

export const Navbar = observer((props: navbar.Props) => (<div className="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom"<h5 className="my-0 mr-md-auto font-weight-normal pointer-cursor" onClick={stores.navigation.root}>Company name</h5>{stores.user.authenticated && (<a href="" className="btn btn-outline-primary" onClick={props.loginOff}>Sign off <i className="fas fa-sign-out-alt"></i></a>)}{!stores.user.authenticated && (<a href="" onClick={props.gotoLoginPage}>Sign in <i className="fas fa-sign-in-alt"></i></a>)}</div>));