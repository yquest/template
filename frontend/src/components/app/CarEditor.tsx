import { observer, propTypes } from "mobx-react";
import * as React from "react";
import { MAKERS, Car } from "../../model/Car";
import { userService } from "../../services/UserService";
import {
  DropDownInput,
  createDefaultDropDownInputStore
} from "../general/DropDownInput";
import { observable, action, computed } from "mobx";
import { AppInput, InputType } from "../general/appTextInput";

const dropDownInputStore = createDefaultDropDownInputStore();

class CarEditorStore {
  @observable
  car: Car = {
    make: null,
    model: null,
    maturityDate: null,
    price: null
  };

  validationsStart = {
    make:true,
    model:true,
    maturityDate:true,
    price:true
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
    this.validationsStart.model = true;
    this.car.model = value;
  }
  @computed
  get modelError():string{
    if(this.validationsStart.model){
      if(this.car.model.length === 0){
        return "required";
      }else{
        return "";
      }
    }
    return null;
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
          style={{
            marginTop: "1rem",
            marginBottom: "1rem",
            padding: "1.5rem"
          }}>
          <div className="col-12">
            <AppInput
              label="Maturity date"
              labelId="maturityDate"
              inputType={InputType.DATE_TIME}
              error={null}
              onChange={value => {
                carEditorStore.updateMaturityDate(value);
              }}
            />
            <AppInput
              label="Model"
              labelId="model"
              inputType={InputType.TEXT}
              error={carEditorStore.modelError}
              onChange={value => {
                carEditorStore.updateDetailModel(value);
              }}
            />
            <AppInput
              label="Price"
              labelId="price"
              inputType={InputType.NUMBER}
              error={null}
              onChange={value => {
                carEditorStore.updateDetailPrice(value);
              }}
            />
            <div className="form-group row">
              <label
                htmlFor="make"
                className="col-2 col-form-label">
                Make
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
          }}>
          save car
        </button>
        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            userService.userLogout();
            localStorage.removeItem("username");
            this.props.logoutEvent();
          }}>
          logout
        </button>
      </div>
    );
  }
}
