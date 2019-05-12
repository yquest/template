export enum MAKERS {
  AUDI, VOLKSWAGEN, NISSAN, PEUGEOT, CITROEN
}

export interface Car {
  make: MAKERS;
  model: string;
  maturityDate: Date;
  price: number;
}

export function carToJson(car:Car){
  let json = (car as any)
  json.make = MAKERS[car.make];
  return json;
}