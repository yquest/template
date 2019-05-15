import { observer } from "mobx-react";
import * as React from "react";
import { action, observable, computed } from "mobx";
import { userService } from "../../services/UserService";
import { User } from "../../model/User";
import { notificationStore, NotificationType } from "./Notifications";
import { AppInput, InputType } from "../general/appTextInput";
import { createGenericStore } from "../GenericStoreValidator";

export interface LoginProps {
  loginSuccessefull(user: string);
  showUserRegister();
}

enum LoginEditorFields {
  LOGIN,
  PASSWORD
}

class LoginStore {
  @observable
  username: string = "";
  @observable
  password: string = "";
  @observable
  startUsernameValidation: boolean = false;
  @observable
  startPasswordValidation: boolean = false;

  @computed
  get passwordError(): string {
    if (!this.startPasswordValidation) {
      return null;
    }
    if (this.password.length === 0) {
      return "required";
    } else {
      return "";
    }
  }

  @computed
  get usernameError(): string {
    if (!this.startUsernameValidation) {
      return null;
    }
    if (this.username.length === 0) {
      return "required";
    } else {
      return "";
    }
  }

  @computed
  get isAllValid() {
    return this.passwordError === "" && this.usernameError === "";
  }

  @action
  updateUserName(value: string) {
    this.startUsernameValidation = true;
    this.username = value;
  }

  @action
  updatePassword(value: string) {
    this.startPasswordValidation = true;
    this.password = value;
  }

  @action
  reset() {
    this.startPasswordValidation = false;
    this.startUsernameValidation = false;
    this.password = "";
    this.username = "";
  }
}

//let loginStore: LoginStore = new LoginStore();

const validation: (idx: LoginEditorFields, value: string) => string = (
  idx,
  value
) => {
  return value.length === 0 ? "required" : "";
};

let loginStore = createGenericStore(2, ()=>"", validation);

@observer
export class LoginEditor extends React.Component<LoginProps, {}> {
  render() {
    return (
      <div>
        <h3>Login</h3>
        <div
          className="card"
          style={{
            marginTop: "1rem",
            marginBottom: "1rem",
            padding: "1.5rem"
          }}>
          <div className="col-12">
            <AppInput
              label="Username"
              labelId="username"
              inputType={InputType.TEXT}
              onChange={value =>
                loginStore.update(LoginEditorFields.LOGIN, value)
              }
              error={loginStore.values[LoginEditorFields.LOGIN].error}
            />
            <AppInput
              label="Password"
              labelId="password"
              inputType={InputType.PASSWORD}
              onChange={value =>
                loginStore.update(LoginEditorFields.PASSWORD, value)
              }
              error={loginStore.values[LoginEditorFields.PASSWORD].error}
            />
          </div>
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            loginStore.checkAllErrors();
            if (!loginStore.isAllValidated) {
              return;
            }
            let user: User = {
              username: loginStore.values[LoginEditorFields.LOGIN].value,
              password: loginStore.values[LoginEditorFields.PASSWORD].value
            };
            userService.userLogin(user).then(res => {
              if (res.status === 200) {
                this.props.loginSuccessefull(user.username);
              } else {
                let notification = notificationStore.createNotification();
                notification.content = "Authentication fails";
                notification.type = NotificationType.ERROR;
                notificationStore.addNotificationTemp(notification, 3000);
              }
              loginStore.reset();
            });
          }}>
          login
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            this.props.showUserRegister();
            loginStore.reset();
          }}>
          sign in
        </button>
      </div>
    );
  }
}
