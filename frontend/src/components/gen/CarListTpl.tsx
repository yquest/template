import * as React from "react";
import { observer } from "mobx-react";
import { carView } from "../app/controllers/CarViewController";
import { stores } from "../../stores/Stores";
const noContent = () => <div>no cars available</div>;
const content = () => {
  return (
    <table className="form-group">
      <thead>
        <tr>
          <th>Make</th>
          <th>Model</th>
          <th>Maturity date</th>
          <th>Price</th>
          {stores.user.authenticated && <th colSpan={2}>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {carView.carViewList()}
      </tbody>
    </table>
  );
};
export const CarList = observer(() =>
  stores.carList.cars.length === 0 ? noContent() : content()
);
