import { Car } from "../model/Car";
import { CarMaker } from "../model/gen/CarMaker";

let car1: Car = {
    make: CarMaker.e.VOLKSWAGEN,
    maturityDate: new Date(1567594959104),
    model: "golf v",
    price: 3000,
    getPK: () => {
        return car1;
    }
};
let car2: Car = {
    make: CarMaker.e.AUDI,
    maturityDate: new Date(1567594959104),
    model: "A6",
    price: 30000,
    getPK: () => {
        return car2;
    }
};

export const cars = [car1,car2];