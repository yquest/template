export enum MAKERS {
  AUDI, VOLKSWAGEN, NISSAN, PEUGEOT, CITROEN
}

export const makerToString: Map<MAKERS, string> = new Map<MAKERS, string>([
  [MAKERS.AUDI, "Audi"],
  [MAKERS.VOLKSWAGEN, "Volkswagen"],
  [MAKERS.NISSAN, "Nissan"],
  [MAKERS.PEUGEOT, "Peugeot"],
]);

export interface RawCar{
  maturityDate:number;
  model:string;
  price:number;
  make:number; 
}

export interface CarPK {
  make: MAKERS;
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

export function carToJson(car: Car) {
  let json = (car as any)
  json.make = MAKERS[car.make];
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