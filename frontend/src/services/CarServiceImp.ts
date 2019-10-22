import Axios, { AxiosResponse } from "axios";
import { Car, MAKERS, CarPK, carFromRaw } from "../model/Car";
import { dateToString } from "../util";
import { CarService } from "./CarService";
import { stores } from "../stores/Stores";

interface RestResult {
    data: any;
    status: number;
}
export class CarServiceImp implements CarService {

    async create(car: Car): Promise<void> {
        let serialized = {
            make: MAKERS[car.make],
            maturityDate: dateToString(car.maturityDate),
            model: car.model,
            price: car.price
        };
        console.log("saved car", serialized);
        const res = await Axios
            .post("api/car", serialized, { withCredentials: true });
        console.log(res.data);
    }

    async update(car: Car): Promise<void> {        
        let serialized = {
            make: MAKERS[car.make],
            maturityDate: dateToString(car.maturityDate),
            model: car.model,
            price: car.price
        };
        console.log("saved car", serialized);
        const res = await Axios
            .put("api/car", serialized, { withCredentials: true });
    }

    async list():Promise<Car[]>{
        const res = await Axios.get("api/car/list");
        return (res.data as Array<any>).map((item) => {
            item.make = MAKERS[item.make];
            item.maturityDate = new Date(item.maturityDate);
            let car = (item as Car);
            car.getPK = () => {
                return {
                    make: car.make,
                    model: car.model
                };
            };
            return item;
        });
    }

    async remove(carPK:Car):Promise<void>{
        await Axios.delete("api/car", {
            params: {
                make: MAKERS[carPK.make],
                model: carPK.model
            }
        });
    }

    get initialList(): Car[] {
        console.log(window["__state"]);
        const initialData = window["__state"];
        const cars = initialData.cars.map(carFromRaw);
        return cars
    }
}

export const carService = new CarServiceImp()