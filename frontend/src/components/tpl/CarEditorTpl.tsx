import { observer } from "mobx-react";
import { carEdit } from "../app/props/CarEditorProps";
import * as React from "react";
import { monthsListCondensed } from "../../util";
import { StyledCalendar } from "./StyledCalendar";
import { SelectInput } from "./SelectInput";
import { stores } from "../../stores/Stores";

export const CarEditor = observer(() => (
  <form onSubmit={carEdit.props.updateCar}>
    <h3>Car {stores.carEdition.title}</h3>
    <div>
      <div className="row">
        <div className="col-md-6">
          <small>date</small>
          <div className="input-group">
            <input
              tabIndex={1}
              type="number"
              placeholder="Year"
              className="form-control"
              value={stores.carEdition.car.maturityDate.getFullYear()}
              onChange={carEdit.props.onChangeYear}
            />
            <SelectInput
              selected={stores.carEdition.car.maturityDate.getMonth()}
              tabIndex={2}
              className="form-control"
              list={monthsListCondensed}
              toKey={(idx, _) => "month" + idx}
              onChange={carEdit.props.onChangeMonth}
            />
            <input
              tabIndex={3}
              type="number"
              placeholder="Day"
              className="form-control"
              value={stores.carEdition.car.maturityDate.getDate()}
              onChange={carEdit.props.onChangeDay}
            />
            <div
              className="input-group-append"
              onClick={carEdit.props.onClickShowCalendar}>
              <span className="input-group-text" style={{ cursor: "pointer" }}>
                <i className={carEdit.props.calendarIconClasses} />
              </span>
            </div>
          </div>
          <StyledCalendar
            open={carEdit.props.openCalendar}
            month={stores.carEdition.car.maturityDate.getMonth() + 1}
            year={stores.carEdition.car.maturityDate.getFullYear()}
            selectChange={carEdit.props.onCalendarChange}
            selected={stores.carEdition.car.maturityDate.getDate()}
          />
        </div>
        <div className="form-group col-md-6">
          <small>hour</small>
          <div className="input-group">
            <input
              tabIndex={4}
              type="number"
              placeholder="Hour"
              className="form-control"
              value={stores.carEdition.car.maturityDate.getHours()}
              onChange={carEdit.props.onChangeHour}
            />
            <input
              tabIndex={5}
              type="number"
              placeholder="Hour"
              className="form-control"
              value={stores.carEdition.car.maturityDate.getMinutes()}
              onChange={carEdit.props.onChangeMinutes}
            />
          </div>
        </div>
      </div>
      <div className="row">
        <div className="col-md-6">{carEdit.createModelInput()}</div>
        <div className="col-md-6">{carEdit.createMakeInput()}</div>
      </div>
      <div className="d-inline">
        <button className="btn btn-primary" tabIndex={3}>
          submit
        </button>
      </div>
      <div className="d-inline col-md-3">
        <button
          className="btn btn-primary"
          tabIndex={4}
          onClick={carEdit.props.onCancel}>
          cancel
        </button>
      </div>
    </div>
  </form>
));
