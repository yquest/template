import { observer } from "mobx-react";
import { carEdit } from "../app/props/CarEditorProps";
import * as React from "react";
import { monthsListCondensed } from "../../util";
import { StyledCalendar } from "./StyledCalendar";
import { SelectInput } from "./SelectInput";

export const CarEditor = observer((props: carEdit.Props) => (
  <form onSubmit={props.onSubmit}>
    <h3>Car {props.title}</h3>
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
              value={props.maturityDate.value.getFullYear()}
              onChange={props.maturityDate.onChangeYear}
            />
            <SelectInput
              tabIndex={2}
              className="form-control"
              list={monthsListCondensed}
              toKey={(idx, _) => "month" + idx}
              onChange={props.maturityDate.onChangeMonth}
            />
            <input
              tabIndex={3}
              type="number"
              placeholder="Day"
              className="form-control"
              value={props.maturityDate.value.getDate()}
              onChange={props.maturityDate.onChangeDay}
            />
            <div
              className="input-group-append"
              onClick={props.maturityDate.onClickShowCalendar}>
              <span className="input-group-text" style={{ cursor: "pointer" }}>
                <i className={props.maturityDate.calendarIconClasses} />
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
              value={props.maturityDate.value.getHours()}
              onChange={props.maturityDate.onChangeHour}
            />
            <input
              tabIndex={5}
              type="number"
              placeholder="Hour"
              className="form-control"
              value={props.maturityDate.value.getMinutes()}
              onChange={props.maturityDate.onChangeMinutes}
            />
          </div>
          <StyledCalendar
            open={props.maturityDate.openedCalendar}
            month={props.maturityDate.value.getMonth() + 1}
            year={props.maturityDate.value.getFullYear()}
            selectChange={props.maturityDate.onCalendarChange}
            selected={props.maturityDate.value.getDate()}
          />
        </div>
      </div>
    </div>
  </form>
));
