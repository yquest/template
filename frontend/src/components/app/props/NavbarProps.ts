import * as React from "react";
import { stores } from "../../../stores/Stores";
import { Navbar } from "../../gen/NavbarTpl";


export namespace navbar {
    export interface Props {
        gotoLoginPage(e:React.MouseEvent<any>):void;
        loginOff(e:React.MouseEvent<any>):void
    }

    export function createProps(): Props {

        function gotoLogin(e:React.MouseEvent<any>){
            e.preventDefault()
            stores.navigation.gotoLogin();
        }

        function loginOff(e:React.MouseEvent<any>){
            e.preventDefault()
            stores.navigation.loginOffEvent();
        }

        const props: Props = {
            loginOff,
            gotoLoginPage: gotoLogin
        };
        return props;
    }

    export function createComponent(): React.FunctionComponentElement<Props> {
        return React.createElement(Navbar, { ...createProps() });
    }
}