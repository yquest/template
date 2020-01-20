import Axios from "axios";
import { Car, carFromRaw } from "../model/Car";
import { CarService } from "./CarService";
import { CarMaker } from "../model/gen/CarMaker";

export class CarServiceImp implements CarService {

    async create(car: Car): Promise<void> {        
        let serialized = {
            make: car.make,
            maturityDate: car.maturityDate.getTime(),
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
            make: car.make,
            maturityDate: car.maturityDate.getTime(),
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
            console.log("map car");
            item.make = CarMaker.getLabel(item.make);
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
        console.log("pause");
        await Axios.delete("api/car", {
            params: {
                make: carPK.make,
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