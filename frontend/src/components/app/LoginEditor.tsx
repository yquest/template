import { observer } from "mobx-react";
import * as React from "react";
import { action, observable } from "mobx";
import { userService } from "../../services/UserService";
import { UserRegisterEditor } from "./UserRegisterEditor";
import { User } from "../../model/User";

export interface LoginProps {
  loginSuccessefull(user: string);
  showUserRegister();
}

class LoginStore {
  @observable
  username: string = "";
  @observable
  password: string = "";

  @action
  updateUserName(value: string) {
    this.username = value;
  }

  @action
  updatePassword(value: string) {
    this.password = value;
  }
}

let loginStore: LoginStore = new LoginStore();

@observer
export class LoginEditor extends React.Component<LoginProps, {}> {
  render() {
    return (
      <div>
        <h3>Login</h3>
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
                  loginStore.updateUserName(event.target.value)
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
                  loginStore.updatePassword(event.target.value)
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
            let user:User = {
              username : loginStore.username,
              password : loginStore.password
            };
            userService.userLogin(user).then((res)=>{
              if(res.status === 200){
                this.props.loginSuccessefull(user.username);
              }
            });
          }}
        >
          login
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => this.props.showUserRegister()}
        >
          sign in
        </button>
      </div>
    );
  }
}
