import { Car } from "../model/Car";

export interface CarService {
    create(car:Car):Promise<void>;
    initialList: Car[];
    list(): Promise<Car[]>;
    update(index:number,car: Car): Promise<void>;
    remove(index:number): Promise<void>;
}