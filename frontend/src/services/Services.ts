import { CarService } from "./CarService";
import { UserService } from "./UsrService";
import { carService } from "./CarServiceImp";
import { userService } from "./UserService";

export interface Services{
    carService:CarService;
    userService:UserService;
}

export const services:Services = {
    carService : carService,
    userService: userService
}