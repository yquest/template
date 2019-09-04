import { Car, MAKERS } from "../model/Car";

let car1: Car = {
    make: MAKERS.VOLKSWAGEN,
    maturityDate: new Date(1567594959104),
    model: "golf v",
    price: 3000,
    getPK: () => {
        return car1;
    }
};
let car2: Car = {
    make: MAKERS.AUDI,
    maturityDate: new Date(1567594959104),
    model: "A6",
    price: 30000,
    getPK: () => {
        return car2;
    }
};

export const cars = [car1,car2];