import { Control } from "./components/events/Page2Events";
import { Car, MAKERS } from "./model/Car"


const cars = [
    {
        make:MAKERS[MAKERS.AUDI],
        model:"A3",
        maturityDate:new Date(),
        price:"30000"
    },
    {
        make:MAKERS[MAKERS.AUDI],
        model:"A3",
        maturityDate:new Date(),
        price:"30000"
    }
]

window["__state"] = {
    auth:true,
    username:"xico",
    cars:cars,
    initialState: {
        input1:'aaa',
        input2:'bbb'
    }
}