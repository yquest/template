import { Car } from "../../../model/Car";

export interface CarManager {    
    car: Car;
    edit: () => void;
    remove: () => void;
}