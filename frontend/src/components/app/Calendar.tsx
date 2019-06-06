import * as React from "react";

function getLastDay(month: number, year: number): Date {
  return new Date(year, month, 0);
}

function getFirstDay(month: number, year: number): Date {
  return new Date(year, month - 1, 1);
}
const weekStr = ["S", "M", "T", "W", "T", "F", "S"];
const weekKey = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
export const monthsStr = [
  "jan",
  "feb",
  "mar",
  "apr",
  "may",
  "jun",
  "jul",
  "ago",
  "sep",
  "oct",
  "nov",
  "dec"
];

export interface CalendarProps{
  className?:string;
  year: number;
  month: number;
  selectChange: (number) => void;
  selected?: number;
  open?:boolean;
}

export const Calendar = (props: CalendarProps) => {
  let yOffeset = 15;
  let selected: number = props.selected | 0;
  let firstDayDate: Date = getFirstDay(props.month, props.year);
  let lastDayDate: Date = getLastDay(props.month, props.year);

  let lastDay = lastDayDate.getDate();
  let lastDayWeekDay = lastDayDate.getDay();
  let firstDayWeekDay = firstDayDate.getDay();

  let days = [];

  let weekTr = weekStr.map((v, k) => (
    <text
      x={k * 20 + 25}
      y={yOffeset + 15}
      key={"week-" + weekKey[k]}
      style={{
        textAnchor: "middle",
        fontSize: "10px",
        fontFamily: "Verdana",
        fontWeight: "bold"
      }}>
      {weekStr[k]}
    </text>
  ));

  var row = 0;
  var column = 0;
  for (let i = 1 - firstDayWeekDay; i <= lastDay + (6 - lastDayWeekDay); i++) {
    if (i < 1 || i > lastDay) {
    } else {
      days.push([
        selected === i && (
          <rect
            key={"selected-" + i}
            x={column * 20 + 16}
            y={row * 20 + yOffeset + 24}
            rx="3"
            ry="3"
            width="18"
            height="14"
            style={{ fill: "rgba(0, 123, 255, .5)" }}
          />
        ),
        <text
          x={column * 20 + 25}
          y={row * 20 + yOffeset + 35}
          className={selected === i ? "selected" : null}
          onClick={() => props.selectChange(i)}
          key={"day-" + i}
          style={{
            textAnchor: "middle",
            fontSize: "10px",
            fontFamily: "Verdana",
            cursor: "pointer"
          }}>
          {i}
        </text>
      ]);
    }
    column++;
    if (i % 7 == (7 - firstDayWeekDay)%7) {
      row++;
      column = 0;
    }
  }

  return (
    <svg viewBox="0 0 170 180" width="20rem" height="20rem" className={props.className}>
      <rect
        x="10"
        y="10"
        rx="10"
        ry="10"
        width="150"
        height={15 + yOffeset + row * 20}
        style={{ fill: "rgba(223, 223, 223, .50)", stroke: "black", strokeWidth: 2 }}
      />
      <line x1="50" y1="50" x2="200" y2="50" />
      {weekTr}
      {days}
      <line
        x1="15"
        y1={yOffeset + 20}
        x2="155"
        y2={yOffeset + 20}
        style={{ stroke: "black", strokeWidth: 1 }}
      />
    </svg>
  );
};
