import { CarService } from "./CarService";
import { UserService } from "./UserService";
import { CarServiceImp} from "./CarServiceImp";
import { UserServiceImp} from "./UserServiceImp";    

export interface Services{
    carService:CarService;
    userService:UserService;
}

export const services:Services = {
    carService : new CarServiceImp(),
    userService: new UserServiceImp()
};