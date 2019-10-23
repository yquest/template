import { appInput } from "../../../controllers/AppInputController";
import * as React from "react";
import { services } from "../../../services/Services";
import { stores } from "../../../stores/Stores";
import { Register } from "../../gen/RegisterTpl";
import { uiStore, NotificationType } from "../../../stores/UIStore";

export namespace registerPage {
    const usernameStore = appInput.createStore(null);
    const passwordStore = appInput.createStore(null);
    const emailStore = appInput.createStore(null);

    export interface Props {
        username: appInput.Props;
        password: appInput.Props;
        email: appInput.Props;
        submitForm: React.FormEventHandler<HTMLFormElement>;
    }

    export function createProps(): Props {
        function mandatoryAppInput(this: appInput.Props): void {
            if ((this.value || "").length === 0) {
                this.updateError(`${this.label} is a mandatory value`);
            }
            else this.updateError("");
        }
        const username: appInput.Props = appInput.createAppInputProps({
            labelId: "username",
            inputType: appInput.Type.TEXT,
            label: "Username",
            store: usernameStore,
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
        const email: appInput.Props = appInput.createAppInputProps({
            labelId: "email",
            inputType: appInput.Type.TEXT,
            label: "Email",
            store: emailStore,
            tabIndex: 3,
            placeholder: null
        });
        function hasErrors(): boolean {
            return username.error !== null && username.error.length > 0 ||
                password.error !== null && password.error.length > 0 ||
                email.error !== null && email.error.length > 0
        }
        function submitForm(e: React.FormEvent): void {
            e.preventDefault();
            mandatoryAppInput.bind(username)();
            mandatoryAppInput.bind(password)();
            mandatoryAppInput.bind(email)();

            console.log(hasErrors());
            if(hasErrors()) return;
            
            services.userService.registerUser({
                username: username.value,
                password: password.value,
                email: email.value
            }).then(_=>{
                const notification = {
                    content:"User created",
                    id:`id-${uiStore.notifications.length}`,
                    type:NotificationType.SUCCESS
                };
                const index = uiStore.notifications.length;
                uiStore.updateNotification(notification);
                setTimeout(() => {
                    uiStore.removeNotification(index);                    
                }, 3000);
                stores.navigation.gotoLogin();
            }).catch(e=>{
                console.error("Oh no!!!",e)
            });
        }

        const props: Props = {
            username: username,
            password,
            email,
            submitForm
        };
        return props;
    }

    export function createComponent(): React.FunctionComponentElement<Props> {
        return React.createElement(Register, { ...createProps() });
    }
}