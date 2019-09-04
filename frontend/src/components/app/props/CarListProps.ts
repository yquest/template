import { Car } from "../../../model/Car";
import { CarManager } from "./CarManager";

export namespace carList {
    export interface Props {
        authenticated:boolean;
        cars:Car[];
        carManagerCreator:(car)=>CarManager;
    }
}
