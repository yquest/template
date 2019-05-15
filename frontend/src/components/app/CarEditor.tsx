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
import { createGenericStore } from "../GenericStoreValidator";

const dropDownInputStore = createDefaultDropDownInputStore();

enum CarEditorFields{
  MAKE,MODEL,MATURITY_DATE,PRICE
}

const validation:(idx:number,value:string)=>string=(idx, value)=>{
  return value.length === 0?"required":"";
}

const carEditorStore = createGenericStore(4,()=>"",validation)

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
          <div className="col-12">
            <AppInput
              label="Maturity date"
              labelId="maturityDate"
              inputType={InputType.DATE_TIME}
              error={carEditorStore.values[CarEditorFields.MATURITY_DATE].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MATURITY_DATE,value);
              }}
            />
            <AppInput
              label="Model"
              labelId="model"
              inputType={InputType.TEXT}
              error={carEditorStore.values[CarEditorFields.MODEL].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MODEL,value);
              }}
            />
            <AppInput
              label="Price"
              labelId="price"
              inputType={InputType.NUMBER}
              error={carEditorStore.values[CarEditorFields.PRICE].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.PRICE,value);
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
              current={carEditorStore.values[CarEditorFields.MAKE].value}
              element={MAKERS}
              updateValue={make => carEditorStore.update(CarEditorFields.MAKE,make)}
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
            console.log("for now, just print car:", carEditorStore.values)
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
