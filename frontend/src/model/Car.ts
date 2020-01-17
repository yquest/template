import { CarMaker } from "./gen/CarMaker";

export interface RawCar{
  maturityDate:number;
  model:string;
  price:number;
  make:number; 
}

export interface CarPK {
  make: CarMaker.e;
  model: string;
}

export interface Car extends CarPK {
  maturityDate: Date;
  price: number;
  getPK: () => CarPK
}

export function carToRaw(car:Car):RawCar{
  return {
    make:car.make,
    maturityDate:car.maturityDate.getTime(),
    model:car.model,
    price:car.price
  }
}

export function createRawCar(car:Car):{
  make:number;
  model:string;
  maturityDate:number;
  price:number;
}{
  return {
    make: car.make,
    model: car.model,
    maturityDate: car.maturityDate.getTime(),
    price: car.price
  };
}

export function carToJson(car: Car):string {
  let json = (car as any)
  json.make = CarMaker[car.make];
  delete (json.getPK);
  json.maturityDate = json.maturityDate.getTime();
  return json;
}

export function carFromRaw(raw: RawCar): Car {
  return {
    maturityDate: new Date(raw.maturityDate),
    make: raw.make,
    price: raw.price,
    model: raw.model,
    getPK: () => this
  };
}
