import { Services } from "../services/Services";
import { CarServiceTest } from "../tests/CarServiceTest";
import { UserServiceTest } from "./UserServiceTest";

export const servicesTest: Services = {
    carService: new CarServiceTest(),
    userService: new UserServiceTest()
};