import { Car } from "../../../model/Car";

export interface CarService {    
    car: Car;
    edit: () => void;
    remove: () => void;
}