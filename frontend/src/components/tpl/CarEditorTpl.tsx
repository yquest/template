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
    <div className="row">
      <div className="form-group col-sm-10 col-md-8 col-lg-6 mb-3 mb-sm-3">
        <div>
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
        </div>
        <div>
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
          <StyledCalendar
            open={carEdit.props.openCalendar}
            month={stores.carEdition.car.maturityDate.getMonth() + 1}
            year={stores.carEdition.car.maturityDate.getFullYear()}
            selectChange={carEdit.props.onCalendarChange}
            selected={stores.carEdition.car.maturityDate.getDate()}
          />
        </div>
        <button className="btn btn-primary" tabIndex={3}>
        submit
      </button>
      </div>
    </div>
  </form>
));
