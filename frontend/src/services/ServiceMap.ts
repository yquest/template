import { Services } from "./Services";
import { CarServiceImp } from "./CarServiceImp";
import { UserServiceTest } from "../tests/UserServiceTest";

export const services:Services = {
    carService: new CarServiceImp(),
    userService: new UserServiceTest()
}