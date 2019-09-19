import { Car } from "../../../model/Car";
import { CarManager } from "./CarManager";
import { page } from "./PageProps";

export namespace app{
    export enum AppState {
        LIST_NO_AUTH,
        CAR_EDIT_AUTH,
        LOG_IN_NO_AUTH,
        REGISTER_NO_AUTH
    }
    export interface Props extends page.Props{
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
}
