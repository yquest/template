export enum MAKERS {
  AUDI, VOLKSWAGEN, NISSAN, PEUGEOT, CITROEN
}

export interface CarPK {
  make: MAKERS;
  model: string;
}

export interface Car extends CarPK{
  maturityDate: Date;
  price: number;
  getPK:()=>CarPK
}

export function carToJson(car:Car){
  let json = (car as any)
  json.make = MAKERS[car.make];
  return json;
}