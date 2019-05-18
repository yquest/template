import { observer } from "mobx-react";
import * as React from "react";
import { action, observable } from "mobx";
import { userService } from "../../services/UserService";
import { User } from "../../model/User";
import { notificationStore, NotificationType } from "./Notifications";
import { AppInput, InputType } from "../general/appTextInput";
import { GenericStore, createGenericStore } from "../GenericStoreValidator";

enum UserRegisterEditorFields {
  USERNAME,
  PASSWORD,
  EMAIL
}

const validation = (idx: number, value: string) => {
  if(value.length === 0){
    console.log("no "+UserRegisterEditorFields[idx]);
    return "required"
  }

  return "";
};

let userRegisterStore: GenericStore = createGenericStore(
  3,
  idx => "",
  validation
);

export interface UserRegisterProps {
  returnToLoginClick(): void;
  successefullyRegistered(): void;
}

@observer
export class UserRegisterEditor extends React.Component<UserRegisterProps, {}> {
  render() {
    return (
      <div>
        <h3>User register</h3>
        <div
          className="card"
          style={{
            marginTop: "1rem",
            marginBottom: "1rem",
            padding: "1.5rem"
          }}>
          <AppInput
            inputType={InputType.TEXT}
            onChange={value =>
              userRegisterStore.update(UserRegisterEditorFields.USERNAME, value)
            }
            label={"username"}
            error={
              userRegisterStore.values[UserRegisterEditorFields.USERNAME].error
            }
            labelId={"username"}
          />
          <AppInput
            inputType={InputType.PASSWORD}
            onChange={value =>
              userRegisterStore.update(UserRegisterEditorFields.PASSWORD, value)
            }
            label={"password"}
            error={
              userRegisterStore.values[UserRegisterEditorFields.PASSWORD].error
            }
            labelId={"password"}
          />
          <AppInput
            inputType={InputType.TEXT}
            onChange={value =>
              userRegisterStore.update(UserRegisterEditorFields.EMAIL, value)
            }
            label={"email"}
            error={
              userRegisterStore.values[UserRegisterEditorFields.EMAIL].error
            }
            labelId={"email"}
          />
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            let user: User = {
              username:
                userRegisterStore.values[UserRegisterEditorFields.USERNAME]
                  .value,
              password:
                userRegisterStore.values[UserRegisterEditorFields.PASSWORD]
                  .value,
              email:
                userRegisterStore.values[UserRegisterEditorFields.EMAIL].value
            };
            userRegisterStore.checkAllErrors();
            let isAllValidated = userRegisterStore.isAllValidated;
            console.log("is all validated?",isAllValidated)
            if (!userRegisterStore.isAllValidated) {
              return;
            }

            userService.registerUser(user).then(res => {            
              let notification = notificationStore.createNotification();
              if (res.status === 204) {
                notification.content = "Success creating user";
                this.props.successefullyRegistered();
                userRegisterStore.reset();
              } else {
                notification.content = "Error when try to create user";
                notification.type = NotificationType.ERROR;
              }
              notificationStore.addNotificationTemp(notification, 3000);
            });
          }}>
          save user
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            this.props.returnToLoginClick();
          }}>
          go back to login
        </button>
      </div>
    );
  }
}
