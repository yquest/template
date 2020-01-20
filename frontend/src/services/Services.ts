import { CarService } from "./CarService";
import { UserService } from "./UserService";
import { CarServiceImp} from "./CarServiceImp";
import { UserServiceImp} from "./UserServiceImp";    
import { CarServiceTest } from "../tests/CarServiceTest";
import { UserServiceTest } from "../tests/UserServiceTest";

export interface Services{
    carService:CarService;
    userService:UserService;
}

export const services:Services = __APP_SERVICES_TEST__?{
    carService : new CarServiceTest(),
    userService: new UserServiceTest()
}:{
    carService : new CarServiceImp(),
    userService: new UserServiceImp()
};