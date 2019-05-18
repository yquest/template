import { observer, propTypes } from "mobx-react";
import * as React from "react";
import { MAKERS, Car } from "../../model/Car";
import { userService, RestResult } from "../../services/UserService";
import {
  DropDownInput,
  createDefaultDropDownInputStore
} from "../general/DropDownInput";
import { AppInput, InputType } from "../general/appTextInput";
import { createGenericStore } from "../GenericStoreValidator";
import { carService } from "../../services/CarService";
import { notificationStore, NotificationType } from "./Notifications";

const dropDownInputStore = createDefaultDropDownInputStore();

enum CarEditorFields {
  MAKE,
  MODEL,
  MATURITY_DATE,
  PRICE
}

const validation: (idx: number, value: string) => string = (idx, value) => {
  return value.length === 0 ? "required" : "";
};

const carEditorStore = createGenericStore(4, () => "", validation);

export interface CarEditorProps {
  saveCarEvent: (car) => void;
  logoutEvent: () => void;
}

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
          <div className="form-group row">
            <AppInput
              label="Maturity date"
              labelId="maturityDate"
              placeholder="maturity date"
              inputType={InputType.DATE_TIME}
              error={carEditorStore.values[CarEditorFields.MATURITY_DATE].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MATURITY_DATE, value);
              }}
            />
            <AppInput
              label="Model"
              labelId="model"
              inputType={InputType.TEXT}
              error={carEditorStore.values[CarEditorFields.MODEL].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MODEL, value);
              }}
            />
            <AppInput
              label="Price"
              labelId="price"
              inputType={InputType.NUMBER}
              error={carEditorStore.values[CarEditorFields.PRICE].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.PRICE, value);
              }}
            />
            <DropDownInput
              current={carEditorStore.values[CarEditorFields.MAKE].value}
              element={MAKERS}
              updateValue={make =>
                carEditorStore.update(CarEditorFields.MAKE, make)
              }
              store={dropDownInputStore}
              error={carEditorStore.values[CarEditorFields.MAKE].error}
            />
          </div>
        </div>

        <button
          style={{ marginRight: "0.5rem" }}
          type="button"
          className="btn btn-primary"
          onClick={() => {
            carEditorStore.checkAllErrors();
            if (carEditorStore.isAllValidated) {
              let values = carEditorStore.values;
              let car: Car = {
                make: values[CarEditorFields.MAKE].value,
                maturityDate: new Date(values[CarEditorFields.MATURITY_DATE].value),
                model: values[CarEditorFields.MODEL].value,
                price: Number.parseInt(values[CarEditorFields.PRICE].value)
              };
              
              let createPromise:Promise<RestResult> = carService.createCar(car);
              let notification = notificationStore.createNotification();
              createPromise.then(res=>{
                notification.content = "Created car succesefully";
                notification.type = NotificationType.SUCCESS;
              });
              createPromise.catch(res=>{
                notification.content = "Error inserting car";
                notification.type = NotificationType.ERROR;
              });
              notificationStore.addNotificationTemp(notification,3000);
            }
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
