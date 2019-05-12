import Axios, { AxiosResponse } from "axios";
import { Car, MAKERS } from "../model/Car";
interface RestResult{
    data:any;
    status:number;
}
export class CarService {
    createCar(car: Car): Promise<RestResult> {
        console.log("saved car",car);
        return Axios
            .post("api/car", {...car,make:MAKERS[car.make]}, { withCredentials: true })
            .then(res => {                
                console.log(res.data);
                return res as RestResult
            });
    }

    getCar(car: Car): Promise<any> {
        return Axios.get("api/car")
    };

    fetchCars(): Promise<Car[]> {
        return Axios.get("api/car/list")
            .then(res => res.data as Array<any>)
            .then(res => {
                return res.map((item) => { 
                    item.make = MAKERS[item.make];
                    item.maturityDate = new Date(item.maturityDate);
                    return item;
                })
            });
    };

}

export const carService = new CarService()