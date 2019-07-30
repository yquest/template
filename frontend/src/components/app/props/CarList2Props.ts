import { Car } from "../../../model/Car";
import { CarManager } from "./CarManager";

export namespace carList2 {
    export interface Props {
        authenticated:boolean;
        cars:Car[];
        carManager:CarManager;
    }
}
