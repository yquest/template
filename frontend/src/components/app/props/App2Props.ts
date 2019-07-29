import { Car } from "../../../model/Car";

export namespace app2{
    export enum AppState {
        LIST_NO_AUTH,
        CAR_EDIT_AUTH,
        LOG_IN_NO_AUTH,
        REGISTER_NO_AUTH
    }
    export interface Props{
        cars:Car[];
        authenticated: boolean;
        appState: AppState;
        username: string;
        loginOn:()=>void
        loginOff:()=>void
    }    
}