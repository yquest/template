import Axios, { AxiosResponse } from "axios";
import { Car, MAKERS, CarPK, carFromJson } from "../model/Car";
import { dateToString } from "../util";

interface RestResult{
    data:any;
    status:number;
}
export class CarService {
    createCar(car: Car): Promise<RestResult> {
        let serialized = {
            make:MAKERS[car.make],
            maturityDate:dateToString(car.maturityDate),
            model:car.model,
            price:car.price
        };
        console.log("saved car",serialized);
        return Axios
            .post("api/car", serialized, { withCredentials: true })        
            .then(res => {                
                console.log(res.data);
                return res as RestResult
            });
    }

    updateCar(car: Car): Promise<RestResult> {
        let serialized = {
            make:MAKERS[car.make],
            maturityDate:dateToString(car.maturityDate),
            model:car.model,
            price:car.price
        };
        console.log("saved car",serialized);
        return Axios
            .put("api/car", serialized, { withCredentials: true })        
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
                    let car = item as Car;
                    car.getPK = ()=>{
                        return {
                            make: car.make,
                            model: car.model
                        }
                    }
                    return item;
                })
            });
    };
    removeCar(carPK: CarPK): Promise<any> {
        return Axios.delete("api/car",{
            params:{
                make:MAKERS[carPK.make],
                model:carPK.model
            }
        })
    };    
    get initialList():Car[]{
        const initialData = window["__state"];
        const cars = initialData.cars.map(carFromJson);
        return cars
    }
}

export const carService = new CarService()