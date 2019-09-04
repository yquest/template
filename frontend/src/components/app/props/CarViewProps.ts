import  { CarManager } from "./CarManager";
import { Car } from "../../../model/Car";

export namespace carView {
    export interface Props {
        maker: string;
        model: string;
        maturityDate: string;
        price: string;
        authenticated: boolean;
        carManager: CarManager;
        car:Car;
    }
}
