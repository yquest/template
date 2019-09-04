export enum MAKERS {
  AUDI, VOLKSWAGEN, NISSAN, PEUGEOT, CITROEN
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

export function carToJson(car: Car) {
  let json = (car as any)
  json.make = MAKERS[car.make];
  delete (json.getPK);
  json.maturityDate = json.maturityDate.getTime();
  return json;
}

export function carFromJson(json: any): Car {
  return {
    maturityDate: new Date(json.maturityDate),
    make: (MAKERS[json.make as string]),
    price: json.price,
    model: json.model,
    getPK: () => this
  };
}