import { Car } from "../model/Car";

export interface CarService {
    create(car:Car):Promise<void>;
    initialList: Car[];
    list(): Promise<Car[]>;
    update(car: Car): Promise<void>;
    remove(car:Car): Promise<void>;
}