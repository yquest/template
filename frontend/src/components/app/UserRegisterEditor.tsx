import { observer } from "mobx-react";
import * as React from "react";
import { action, observable } from "mobx";
import { userService } from "../../services/UserService";
import { User } from "../../model/User";
import { notificationStore, NotificationType } from "./Notifications";

class UserRegisterStore {
  @observable
  username: string = "";
  @observable
  email: string = "";
  @observable
  password: string = "";

  @action
  updateUserName(value: string) {
    this.username = value;
  }

  @action
  updateEmail(value: string) {
    this.email = value;
  }

  @action
  updatePassword(value: string) {
    this.password = value;
  }
}

let userRegisterStore: UserRegisterStore = new UserRegisterStore();

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
          style={{ marginTop: "1rem", marginBottom: "1rem", padding: "1.5rem" }}
        >
          <div className="col-12">
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Username
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="text"
                onChange={event =>
                  userRegisterStore.updateUserName(event.target.value)
                }
              />
            </div>
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Password
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="password"
                onChange={event =>
                  userRegisterStore.updatePassword(event.target.value)
                }
              />
            </div>
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Email
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="email"
                onChange={event =>
                  userRegisterStore.updateEmail(event.target.value)
                }
              />
            </div>
          </div>
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            let user: User = {
              username: userRegisterStore.username,
              password: userRegisterStore.password,
              email: userRegisterStore.email
            };
            userService.registerUser(user).then(res => {
              let notification = notificationStore.createNotification();
              if (res.status === 204) {
                notification.content = "Success creating user";
                this.props.successefullyRegistered();
              } else {
                notification.content = "Error when try to create user";
                notification.type = NotificationType.ERROR;
              }
              notificationStore.addNotificationTemp(notification, 3000);
            });
          }}
        >
          save user
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            this.props.returnToLoginClick();
          }}
        >
          go back to login
        </button>
      </div>
    );
  }
}
