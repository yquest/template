import { appInput } from "../../../controllers/AppInputController";
import { Login } from "../../gen/LoginTpl";
import * as React from "react";
import { services } from "../../../services/Services";
import { stores } from "../../../stores/Stores";
import { observable, action } from "mobx";


export namespace loginPage {

    export interface Props {
        login: appInput.Props;
        password: appInput.Props;
        onClickRegisterLink(e:React.MouseEvent<any>):void;
        submitForm: React.FormEventHandler<HTMLFormElement>;
        errorFormClasses:string;
        errorForm:string;
        showErrorForm:boolean;
    }

    const loginActions = {updateLogin:action};
    const loginValue = observable({
        login:null as boolean,
        get error():string{
            return "Login fail";
        },
        get errorClasses():string{
            return "invalid-feedback-show";
        },
        get showError():boolean{
            return loginValue.login === false;
        },
        updateLogin(login:boolean){
            loginValue.login = login;
        }
    },loginActions);
    const loginStore = appInput.createStore(null);
    const passwordStore = appInput.createStore(null);

    function clearForm(){
        loginStore.updateValue("");
        loginStore.updateError(null)
        passwordStore.updateValue("");
        passwordStore.updateError(null);
        loginValue.updateLogin(null);
    }

    export function createProps(): Props {
        function mandatoryAppInput(this: appInput.Props): void {
            if ((this.value || "").length === 0) {
                this.updateError(`${this.label} is a mandatory value`);
            }
            else this.updateError("");
        }
        const login: appInput.Props = appInput.createAppInputProps({
            labelId: "login",
            inputType: appInput.Type.TEXT,
            label: "Login",
            store: loginStore,
            tabIndex: 1,
            placeholder: null,
        });
        const password: appInput.Props = appInput.createAppInputProps({
            labelId: "password",
            inputType: appInput.Type.PASSWORD,
            label: "Password",
            store: passwordStore,
            tabIndex: 2,
            placeholder: null
        });

        function loginFail(){
            loginValue.updateLogin(false);
        }

        function loginSuccess(){
            stores.user.updateUser(
                services.userService.authenticated,
                services.userService.name
            );
            clearForm();
            stores.navigation.root();            
        }

        function submitForm(e: React.FormEvent): void {
            e.preventDefault();
            mandatoryAppInput.bind(login)();
            mandatoryAppInput.bind(password)();
            
            services.userService.userLogin({
                user: login.value,
                pass: password.value
            }).then(res => {
                if(res){
                    loginSuccess();
                }else{
                    loginFail();
                }
            }).catch(_ => {
                stores.user.updateUser(false, null);
            });
        }

        function clickOnRegisterLink(e:React.MouseEvent<any>){
            e.preventDefault()
            clearForm();
            stores.navigation.gotoRegister();
        }

        const props: Props = {
            login,
            password,
            submitForm,
            onClickRegisterLink:clickOnRegisterLink,
            errorFormClasses:loginValue.errorClasses,
            errorForm:loginValue.error,
            showErrorForm:loginValue.showError
        };
        return props;
    }

    export function createComponent(): React.FunctionComponentElement<Props> {
        return React.createElement(Login, { ...createProps() });
    }
}