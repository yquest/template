import { observer } from "mobx-react";
import * as React from "react";
import { userService } from "../../services/UserService";
import { User } from "../../model/User";
import { AppInput, InputType } from "../general/AppTextInput";
import { GenericStore, createGenericStore } from "../GenericStoreValidator";
import { uiStore, NotificationType } from "../../UIStore";

enum UserRegisterEditorFields {
  USERNAME,
  PASSWORD,
  EMAIL
}

const validation = (idx: number, value: string) => {
  if (value.length === 0) {
    console.log("no " + UserRegisterEditorFields[idx]);
    return "required";
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

function onSubmitUserRegister(successefullyRegistered) {
  return () => {
    let user: User = {
      username:
        userRegisterStore.values[UserRegisterEditorFields.USERNAME].value,
      password:
        userRegisterStore.values[UserRegisterEditorFields.PASSWORD].value,
      email: userRegisterStore.values[UserRegisterEditorFields.EMAIL].value
    };
    userRegisterStore.checkAllErrors();
    let isAllValidated = userRegisterStore.isAllValidated;
    console.log("is all validated?", isAllValidated);
    if (!userRegisterStore.isAllValidated) {
      return false;
    }

    userService.registerUser(user).then(res => {
      let notification = uiStore.createNotification();
      if (res.status === 204) {
        notification.content = "Success creating user";
        successefullyRegistered();
        userRegisterStore.reset();
      } else {
        notification.content = "Error when try to create user";
        notification.type = NotificationType.ERROR;
      }
      uiStore.addNotificationTemp(notification, 3000);
    });
    return false;
  };
}

@observer
export class UserRegisterEditor extends React.Component<UserRegisterProps, {}> {
  render() {
    return (
      <form onSubmit={onSubmitUserRegister(this.props.successefullyRegistered)}>
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
            currentValue={
              userRegisterStore.values[UserRegisterEditorFields.USERNAME].value
            }
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
            currentValue={
              userRegisterStore.values[UserRegisterEditorFields.PASSWORD].value
            }
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
            currentValue={
              userRegisterStore.values[UserRegisterEditorFields.EMAIL].value
            }
          />
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="submit"
          className="btn btn-primary">
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
      </form>
    );
  }
}
