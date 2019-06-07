import { observer, propTypes } from "mobx-react";
import * as React from "react";
import { MAKERS, Car } from "../../model/Car";
import { userService, RestResult } from "../../services/UserService";
import styled from "styled-components";
import {
  DropDownInput,
  createDefaultDropDownInputStore
} from "../general/DropDownInput";
import { AppInput, InputType } from "../general/AppTextInput";
import { createGenericStore } from "../GenericStoreValidator";
import { carService } from "../../services/CarService";
import { notificationStore, NotificationType } from "./Notifications";
import { Calendar, monthsStr } from "./Calendar";

const dropDownInputStore = createDefaultDropDownInputStore();

enum CarEditorFields {
  MAKE,
  MODEL,
  MATURITY_DATE,
  PRICE,
  SHOW_CALENDAR
}

const validation: (idx: number, value: string) => string = (idx, value) => {
  return value === null || value.length === 0 ? "required" : "";
};

const creator = (idx: number) => {
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


const carEditorStore = createGenericStore(5, creator, validation);
const StyledCalendar = styled(Calendar)`
  height: ${props => (props.open ? "20rem" : "0")};
  transition-timing-function: ease-in-out;
  transition: all 1s;
`;

window["carEditorStore"] = carEditorStore;

export interface CarEditorProps {
  saveCarEvent: (car) => void;
  logoutEvent: () => void;
}

@observer
export class CarEditor extends React.Component<CarEditorProps, {}> {
  render() {
    let maturityDate: Date =
      carEditorStore.values[CarEditorFields.MATURITY_DATE].value;
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
          <h4>New</h4>
          <div className="row">
            <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
              <label>Maturity Date</label>
              <div className="">
                <small>date</small>
                <div className="input-group">
                  <input
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
                      <i className={"fa fa-calendar"+(carEditorStore.values[CarEditorFields.SHOW_CALENDAR].value?"-day":"")} />
                    </span>
                  </div>
                </div>
                <small>hour</small>
                <div className="input-group">
                  <input
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
              label="Model"
              labelId="model"
              inputType={InputType.TEXT}
              error={carEditorStore.values[CarEditorFields.MODEL].error}
              onChange={value => {
                carEditorStore.update(CarEditorFields.MODEL, value);
              }}
              currentValue={carEditorStore.values[CarEditorFields.MODEL].value}
            />
            <AppInput
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
              label="Make"
              current={carEditorStore.values[CarEditorFields.MAKE].value}
              element={MAKERS}
              updateValue={make =>
                carEditorStore.update(CarEditorFields.MAKE, make)
              }
              name="make"
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
                maturityDate: values[CarEditorFields.MATURITY_DATE].value,
                model: values[CarEditorFields.MODEL].value,
                price: Number.parseInt(values[CarEditorFields.PRICE].value)
              };

              let createPromise: Promise<RestResult> = carService.createCar(
                car
              );
              let notification = notificationStore.createNotification();
              let promises: Array<Promise<any>> = [];
              promises.push(
                createPromise.then(res => {
                  notification.content = "Created car succesefully";
                  notification.type = NotificationType.SUCCESS;
                  carEditorStore.reset();
                })
              );
              promises.push(
                createPromise.catch(res => {
                  notification.content = "Error inserting car";
                  console.error(res.response.data.error);
                  notification.type = NotificationType.ERROR;
                })
              );

              Promise.all(promises).finally(() => {
                notificationStore.addNotificationTemp(notification, 3000);
              });
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

window["carStore"] = carEditorStore;
