import { Control } from "./components/events/Page2Events";
import { Car, MAKERS } from "./model/Car"


const cars = [
    {
        make:MAKERS[MAKERS.AUDI],
        model:"A3",
        maturityDate:new Date(1569000000000),
        price:"30000"
    },
    {
        make:MAKERS[MAKERS.NISSAN],
        model:"Note",
        maturityDate:new Date(1569512798493),
        price:"10000"
    }
]

console.log(new Date().getTime());

window["__state"] = {
    auth:true,
    username:"xico",
    cars:cars,
    initialState: {
        input1:'aaa',
        input2:'bbb'
    }
}