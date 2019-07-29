import { Car } from "../../../model/Car";

export interface CarManager {
    edit: (car: Car) => () => void;
    remove: (car: Car) => () => void;
}