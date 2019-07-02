import { observer } from "mobx-react";
import * as React from "react";
import { userService } from "../../services/UserService";
import { User } from "../../model/User";
import { AppInput, InputType } from "../general/AppTextInput";
import { createGenericStore } from "../GenericStoreValidator";
import { uiStore, NotificationType } from "../../UIStore";

export interface LoginProps {
  loginSuccessefull(user: string);
  showUserRegister();
}

enum LoginEditorFields {
  LOGIN,
  PASSWORD
}

const validation: (idx: LoginEditorFields, value: string) => string = (
  idx,
  value
) => {
  return value.length === 0 ? "required" : "";
};

let loginStore = createGenericStore(2, () => "", validation);

@observer
export class LoginEditor extends React.Component<LoginProps, {}> {
  render() {
    return (
      <div>
        <h3>Sign in</h3>
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
              currentValue={loginStore.values[LoginEditorFields.LOGIN].value}
            />
            <AppInput
              label="Password"
              labelId="password"
              inputType={InputType.PASSWORD}
              onChange={value =>
                loginStore.update(LoginEditorFields.PASSWORD, value)
              }
              error={loginStore.values[LoginEditorFields.PASSWORD].error}
              currentValue={loginStore.values[LoginEditorFields.PASSWORD].value}
            />
          </div>
        </div>

        <div className="d-flex justify-content-end">
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
                  let notification = uiStore.createNotification();
                  notification.content = "Authentication fails";
                  notification.type = NotificationType.ERROR;
                  uiStore.addNotificationTemp(notification, 3000);
                }
                loginStore.reset();
              });
            }}>
            Sign in
          </button>
          <button
            style={{ marginRight: "0.5rem" }}
            type="button"
            className="btn btn-primary"
            onClick={() => {
              this.props.showUserRegister();
              loginStore.reset();
            }}>
            Sign up
          </button>
        </div>
      </div>
    );
  }
}
