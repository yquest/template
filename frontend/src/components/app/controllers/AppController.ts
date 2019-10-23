import * as React from "react";
import { stores } from "../../../stores/Stores";

export namespace app{
    export interface Props{
        createCarClick(e:React.MouseEvent<any>):void;
    }

    function createCarClick(e:React.MouseEvent<any>){
        stores.carEdition.createNewCar();
    }

    export function createProps():Props{
        return {createCarClick};
    }
}