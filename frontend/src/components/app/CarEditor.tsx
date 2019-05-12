import { observer, propTypes } from "mobx-react";
import * as React from "react";
import { MAKERS, Car } from "../../model/Car";
import { userService } from "../../services/UserService";
import {
  DropDownInput,
  Store as DropDownInputStore
} from "../general/DropDownInput";
import { observable, action } from "mobx";

class DropDownInputStoreImp implements DropDownInputStore {
  @observable
  isOpen: boolean = false;
  @action
  updateState(isOpen: boolean) {
    this.isOpen = isOpen;
  }
}

let dropDownInputStore: DropDownInputStore = new DropDownInputStoreImp();

class CarEditorStore {
  @observable
  car: Car = {
    make: null,
    model: null,
    maturityDate: null,
    price: null
  };

  @action
  updateMaturityDate(value: string) {
    this.car.maturityDate = new Date(value);
  }

  @action
  updateDetailMake(value: number) {
    this.car.make = value;
  }
  @action
  updateDetailPrice(value: string) {
    this.car.price = Number(value);
  }

  @action
  updateDetailModel(value: string) {
    this.car.model = value;
  }
}

export interface CarEditorProps {
  saveCarEvent: (car) => void;
  logoutEvent: () => void;
}

export const carEditorStore = new CarEditorStore();

@observer
export class CarEditor extends React.Component<CarEditorProps, {}> {
  render() {
    return (
      <div>
        <h3>Car edit</h3>
        <div
          className="card"
          style={{ marginTop: "1rem", marginBottom: "1rem",padding: "1.5rem" }}
        >
          <div className="col-12">
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Maturity date
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="datetime-local"
                onChange={event => {
                  carEditorStore.updateMaturityDate(event.target.value);
                }}
              />
            </div>
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Model
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="text"
                onChange={event => {
                  carEditorStore.updateDetailModel(event.target.value);
                }}
              />
            </div>
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Price
              </label>
            </div>
            <div className="col-6">
              <input
                className="form-control"
                type="number"
                onChange={event => {
                  carEditorStore.updateDetailPrice(event.target.value);
                }}
              />
            </div>
            <div className="form-group row">
              <label
                htmlFor="example-datetime-local-input"
                className="col-2 col-form-label"
              >
                Maker
              </label>
            </div>

            <DropDownInput
              current={carEditorStore.car.make}
              element={MAKERS}
              updateValue={make => carEditorStore.updateDetailMake(make)}
              store={dropDownInputStore}
            />
          </div>
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            this.props.saveCarEvent(carEditorStore.car);
          }}
        >
          save car
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
              userService.userLogout()
              localStorage.removeItem("username");
              this.props.logoutEvent();
            }}
        >
          logout
        </button>
      </div>
    );
  }
}
