import { observer, propTypes } from "mobx-react";
import * as React from "react";
import { MAKERS, Car } from "../../model/Car";
import { RestResult } from "../../services/UserService";
import styled from "styled-components";
import {
  DropDownInput,
  createDefaultDropDownInputStore
} from "../general/DropDownInput";
import { AppInput, InputType } from "../general/AppTextInput";
import { createGenericStore } from "../GenericStoreValidator";
import { carService } from "../../services/CarService";
import { Calendar, monthsStr } from "./Calendar";
import { uiStore, NotificationType } from "../../UIStore";

const dropDownInputStore = createDefaultDropDownInputStore();

enum CarEditorFields {
  MAKE,
  MODEL,
  MATURITY_DATE,
  PRICE,
  SHOW_CALENDAR,
  IS_CREATION_STATE
}

const validation: (idx: number, value: string) => string = (idx, value) => {
  return value === null || value.length === 0 ? "required" : "";
};

const creator = (idx: number) => {
  if (idx === CarEditorFields.IS_CREATION_STATE) {
    return true;
  }
  if (idx === CarEditorFields.MATURITY_DATE) {
    let date = new Date();
    date.setSeconds(0);
    return date;
  }
  if (idx === CarEditorFields.SHOW_CALENDAR) {
    return false;
  }
  if (idx === CarEditorFields.MAKE || idx === CarEditorFields.MATURITY_DATE) {
    return null;
  }
  return "";
};

const carEditorStore = createGenericStore(6, creator, validation);
const StyledCalendar = styled(Calendar)`
  height: ${props => (props.open ? "20rem" : "0")};
  transition-timing-function: ease-in-out;
  transition: all 1s;
`;

export interface CarEditorProps {
  saveCarEvent: (car: Car, creation: boolean) => void;
}

export function updateEditorCar(car: Car | null) {
  console.log("try to update car");
  if (car === null) {
    carEditorStore.reset();
  } else {
    carEditorStore.update(CarEditorFields.MAKE, car.make);
    carEditorStore.update(CarEditorFields.MATURITY_DATE, car.maturityDate);
    carEditorStore.update(CarEditorFields.MODEL, car.model);
    carEditorStore.update(CarEditorFields.PRICE, car.price);
    carEditorStore.update(CarEditorFields.IS_CREATION_STATE, false);
  }
}

@observer
export class CarEditor extends React.Component<CarEditorProps, {}> {
  render() {
    let isCreateCarState: boolean =
      carEditorStore.values[CarEditorFields.IS_CREATION_STATE].value;
    let titleAction = isCreateCarState ? "creation" : "update";
    let maturityDate: Date =
      carEditorStore.values[CarEditorFields.MATURITY_DATE].value;
    return (
      <div>
        <h3>Car {titleAction}</h3>
        <div
          className="card"
          style={{
            marginTop: "1rem",
            marginBottom: "1rem",
            padding: "1.5rem"
          }}>
          <h4>New</h4>
          <div className="row">
            <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
              <label>Maturity Date</label>
              <div className="">
                <small>date</small>
                <div className="input-group">
                  <input
                    tabIndex={1}
                    type="number"
                    placeholder="Year"
                    className="form-control"
                    value={maturityDate.getFullYear()}
                    onChange={e => {
                      let year = Number(e.target.value);
                      if (year !== NaN || year !== 0) {
                        maturityDate.setFullYear(year);
                      }
                      carEditorStore.update(
                        CarEditorFields.MATURITY_DATE,
                        maturityDate
                      );
                    }}
                  />
                  <select
                    tabIndex={2}
                    className="form-control"
                    onChange={e => {
                      maturityDate.setMonth(Number(e.target.value));
                      carEditorStore.update(
                        CarEditorFields.MATURITY_DATE,
                        maturityDate
                      );
                    }}
                    value={maturityDate.getMonth()}>
                    {monthsStr.map((m, i) => (
                      <option key={"month-" + i} value={i}>
                        {m}
                      </option>
                    ))}
                  </select>
                  <input
                    tabIndex={3}
                    type="number"
                    placeholder="Day"
                    className="form-control"
                    value={maturityDate.getDate()}
                    onChange={e => {
                      let day = Number(e.target.value);
                      if (day !== NaN || day !== 0) {
                        maturityDate.setDate(day);
                      }
                      carEditorStore.update(
                        CarEditorFields.MATURITY_DATE,
                        maturityDate
                      );
                    }}
                  />

                  <div
                    className="input-group-append"
                    onClick={() => {
                      carEditorStore.update(
                        CarEditorFields.SHOW_CALENDAR,
                        !carEditorStore.values[CarEditorFields.SHOW_CALENDAR]
                          .value
                      );
                    }}>
                    <span
                      className="input-group-text"
                      style={{ cursor: "pointer" }}>
                      <i
                        className={
                          "fa fa-calendar" +
                          (carEditorStore.values[CarEditorFields.SHOW_CALENDAR]
                            .value
                            ? "-day"
                            : "")
                        }
                      />
                    </span>
                  </div>
                </div>
                <small>hour</small>
                <div className="input-group">
                  <input
                    tabIndex={4}
                    type="number"
                    placeholder="Hour"
                    className="form-control"
                    value={maturityDate.getHours()}
                    onChange={e => {
                      let hour = Number(e.target.value);
                      if (hour !== NaN || hour < 0 || hour > 23) {
                        maturityDate.setHours(hour);
                      }
                      carEditorStore.update(
                        CarEditorFields.MATURITY_DATE,
                        maturityDate
                      );
                    }}
                  />
                  <input
                    tabIndex={5}
                    type="number"
                    placeholder="Hour"
                    className="form-control"
                    value={maturityDate.getMinutes()}
                    onChange={e => {
                      let hour = Number(e.target.value);
                      if (hour !== NaN || hour < 0 || hour >= 60) {
                        maturityDate.setMinutes(hour);
                      }
                      carEditorStore.update(
                        CarEditorFields.MATURITY_DATE,
                        maturityDate
                      );
                    }}
                  />
                </div>
                <StyledCalendar
                  open={
                    carEditorStore.values[CarEditorFields.SHOW_CALENDAR].value
                  }
                  month={maturityDate.getMonth() + 1}
                  year={maturityDate.getFullYear()}
                  selectChange={day => {
                    maturityDate.setDate(day);
                    carEditorStore.update(
                      CarEditorFields.MATURITY_DATE,
                      maturityDate
                    );
                  }}
                  selected={maturityDate.getDate()}
                />
              </div>
            </div>

            <AppInput
              tabIndex={6}
              label="Model"
              labelId="model"
              inputType={InputType.TEXT}
              error={carEditorStore.values[CarEditorFields.MODEL].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MODEL, value);
              }}
              currentValue={carEditorStore.values[CarEditorFields.MODEL].value}
              disabled={!isCreateCarState}
            />
            <AppInput
              tabIndex={7}
              label="Price"
              labelId="price"
              inputType={InputType.NUMBER}
              error={carEditorStore.values[CarEditorFields.PRICE].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.PRICE, value);
              }}
              currentValue={carEditorStore.values[CarEditorFields.PRICE].value}
            />
            <DropDownInput
              tabIndex={8}
              label="Make"
              current={carEditorStore.values[CarEditorFields.MAKE].value}
              element={MAKERS}
              updateValue={make =>
                carEditorStore.update(CarEditorFields.MAKE, make)
              }
              disabled={!isCreateCarState}
              name="make"
              store={dropDownInputStore}
              error={carEditorStore.values[CarEditorFields.MAKE].error}
            />
          </div>
        </div>
        <div className="d-flex justify-content-end">
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
                  maturityDate: values[CarEditorFields.MATURITY_DATE].value,
                  model: values[CarEditorFields.MODEL].value,
                  price: Number.parseInt(values[CarEditorFields.PRICE].value),
                  getPK: () => {
                    return {
                      make: car.make,
                      model: car.model
                    };
                  }
                };
                let saveAction: (car: Car) => Promise<RestResult>;
                if (isCreateCarState) {
                  saveAction = carService.createCar;
                } else {
                  saveAction = carService.updateCar;
                }
                let createPromise: Promise<RestResult> = saveAction(car);
                let notification = uiStore.createNotification();
                let promises: Array<Promise<any>> = [];
                promises.push(
                  createPromise.then(() => {
                    notification.content = "Created car succesefully";
                    notification.type = NotificationType.SUCCESS;
                    carEditorStore.reset();
                    this.props.saveCarEvent(car, isCreateCarState);
                  })
                );
                promises.push(
                  createPromise.catch(res => {
                    if (res.response.status === 401) {
                      notification.content = "Car already exists";
                    } else {
                      notification.content = "Error inserting car";
                    }
                    console.error(res.response.data.error);
                    notification.type = NotificationType.ERROR;
                  })
                );

                Promise.all(promises).finally(() => {
                  uiStore.addNotificationTemp(notification, 3000);
                });
              }
            }}>
            save car
          </button>
          {!carEditorStore.values[CarEditorFields.IS_CREATION_STATE].value && (
            <button
              style={{ marginRight: "0.5rem" }}
              type="button"
              className="btn btn-primary"
              onClick={() => carEditorStore.reset()}>
              cancel update
            </button>
          )}
        </div>
      </div>
    );
  }
}
