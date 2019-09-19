import { Car } from "../../../model/Car";

export interface CarManager {    
    car: Car;
    edit: (e) => void;
    remove: (e) => void;
}