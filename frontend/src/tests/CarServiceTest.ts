import { CarService } from "../services/CarService";
import { Car, carFromJson } from "../model/Car";

export class CarServiceTest implements CarService {
    carList: Car[];
    constructor() {
        const initialData = window["__state"];
        this.carList = initialData.cars.map(carFromJson);
    }
    create(car: Car): Promise<void> {
        this.carList.push(car);
        return Promise.resolve();
    }
    get initialList(): Car[] {
        return this.carList;
    }
    list(): Promise<Car[]> {
        return Promise.resolve(this.carList);
    }
    update(index: number, car: Car): Promise<void> {
        this.carList[index] = car;
        return Promise.resolve();
    }
    remove(index: number): Promise<void> {
        function reducer(acc: Car[], cur: Car, curIndex: number): Car[] {
            if (index !== curIndex) {
                acc.push(cur);
            }
            return acc;
        }

        this.carList = this.carList.reduce(reducer, []);
        return Promise.resolve();
    }

}