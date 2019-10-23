//import { services as serviceMap} from "../services/ServiceMap";
import { servicesTest } from "../tests/ServiceTestMap";
import { CarService } from "./CarService";
import { UserService } from "./UsrService";

export interface Services{
    carService:CarService;
    userService:UserService;
}

export const services:Services = servicesTest;