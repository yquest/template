export namespace page{
    export enum AppState {
        LIST_NO_AUTH,
        CAR_EDIT_AUTH,
        LOG_IN_NO_AUTH,
        REGISTER_NO_AUTH
    }
    export interface Props{
        authenticated: boolean;
        appState: AppState;
        username: string;
        loginOn:(e)=>void;
        loginOff:(e)=>void;
        pageActions:PageActions;
    }
}
