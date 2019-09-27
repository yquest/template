import { app } from "./AppProps";
import { PageActions } from "./PageActions";

export namespace navbar {
  export interface Props {
    appState: app.AppState;
    loginOff: (e) => void;
    loginOn: (e) => void;
    pageActions:PageActions;
  }
}
