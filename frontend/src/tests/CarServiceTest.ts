import { CarService } from "../services/CarService";
import { Car, carFromRaw, carToRaw } from "../model/Car";

export class CarServiceTest implements CarService {
    carList: Car[];
    constructor() {
        const initialData = JSON.parse(localStorage.getItem("cars"));
        this.carList = initialData.map(carFromRaw);
    }

    private saveInStore(){
        localStorage.setItem("cars",JSON.stringify(this.carList.map(carToRaw)));
    }

    create(car: Car): Promise<void> {
        this.carList.push(car);
        this.saveInStore();
        return Promise.resolve();
    }
    get initialList(): Car[] {
        return this.carList;
    }
    list(): Promise<Car[]> {
        return Promise.resolve(this.carList);
    }
    update(car: Car): Promise<void> {
        const index = this.carList.findIndex(current=>{
            current.make === car.make && current.model === car.model;
        });
        this.carList[index] = car;
        this.saveInStore();
        return Promise.resolve();
    }
    remove(car: Car): Promise<void> {
        function reducer(acc: Car[], cur: Car): Car[] {
            if (cur.make !== car.make || cur.model !== car.model) {
                acc.push(cur);
            }
            return acc;
        }

        this.carList = this.carList.reduce(reducer, []);
        this.saveInStore();
        return Promise.resolve();
    }

}