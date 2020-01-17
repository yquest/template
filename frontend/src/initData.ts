import {  RawCar, carToRaw, carFromRaw } from "./model/Car"
import { CarMaker } from "./model/gen/CarMaker"


const cars:RawCar[] = [
    {
        make:CarMaker.e.AUDI,
        model:"A3",
        maturityDate:1569000000000,
        price:30000
    },
    {
        make:CarMaker.e.NISSAN,
        model:"Note",
        maturityDate:1569512798493,
        price:10000
    }
]

//localStorage.setItem("cars",JSON.stringify(cars.map(carFromRaw)));


window["__state"] = {
    auth:true,
    username:"xico",
    cars:cars,
    initialState: {
        input1:'aaa',
        input2:'bbb'
    }
}