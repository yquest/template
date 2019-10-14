import * as React from "react";
import { stores } from "../../stores/Stores";

export const Navbar = () => (
  <div>
    <div className="d-flex flex-column flex-md-row align-items-center p-3 px-md-4 mb-3 bg-white border-bottom">
      <h5
        className="my-0 mr-md-auto font-weight-normal pointer-cursor"
        onClick={stores.navigation.root}>
        Company name
      </h5>
      <a
        href=""
        className="p-2 text-dark"
        onClick={stores.navigation.gotoPage2}>
        Page 2
      </a>
      {stores.user.authenticated && (
        <a href="" className="btn btn-outline-primary" onClick={stores.navigation.loginOffEvent}>
          Sign off <i className="fas fa-sign-out-alt"></i>
        </a>
      )}
      {!stores.user.authenticated && (
        <a href="" onClick={stores.navigation.loginPage}>
          Sign in <i className="fas fa-sign-in-alt"></i>
        </a>
      )}
    </div>
  </div>
);
