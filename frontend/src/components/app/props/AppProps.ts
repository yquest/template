import { Car } from "../../../model/Car";
import { CarManager } from "./CarManager";
import { page } from "./PageProps";
import { observable, action, IObservableArray } from "mobx";

export namespace app{
    export enum AppState {
        LIST_NO_AUTH,
        CAR_EDIT_AUTH,
        LOG_IN_NO_AUTH,
        REGISTER_NO_AUTH
    }
    export interface Props extends page.Props{
        pageActions:PageActions;
        cars:Car[];
        carManagerCreator:(car:Car)=>CarManager;
    }
    export enum InputType {
        TEXT,
        PASSWORD,
        SELECT,
        DATE_TIME,
        NUMBER
      }      
    export interface TextInputProps{
        onChange(value: string): void;
        inputType: InputType;
        label: string;
        labelId: string;
        error: string;
        placeholder?: string;
        disabled?:boolean;
        currentValue: string;
        tabIndex?:number;      
    }

    export const carStore = observable({
        cars:[] as IObservableArray<Car>,
        init(cars:Car[]) {
            carStore.cars.clear();
            for(let idx in cars){
                carStore.cars.push(cars[idx])
            }
        },
        remove(car:Car){
            carStore.cars.remove(car);
        },
        update(car:Car){
            carStore.cars.filter((current)=>current.make === car.make && current.model === car.model);
            carStore.cars.push(car);
        }
    },{
        init:action, remove:action, update:action
    });
}
