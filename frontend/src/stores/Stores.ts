import { observable, IObservableArray, action, configure } from "mobx";
import { Car } from "../model/Car";
import { userService } from "../services/UserService";
import { services } from "../services/Services";
import { CarMaker } from "../model/gen/CarMaker";

configure({ enforceActions: "observed" });

const routerActions = { update: action };
const router = observable({
    page: window["__state"]["page"] as string,
    update(page: string): void {
        router.page = page;
    }, get isRootPage(): Boolean {
        return (router.page || "/") == "/";
    }
}, routerActions);

const carListActions = { remove: action, update: action, updateCars: action, createCar:action };
const carList = observable({
    cars: services.carService.initialList as IObservableArray<Car>,
    remove(car: Car) {
        carList.cars.remove(car)
    },
    updateCars(cars: Car[]) {
        carList.cars.replace(cars);
    },
    createCar(car:Car){
        carList.cars.push(car);
    },
    update(idx: number, car: Car): void {
        carList.cars[idx] = { ...car };
    },
}, carListActions);

const userActions = { updateName: action, updateAuthenticated: action, updateUser: action };
const user = observable({
    name: userService.initialName,
    authenticated: userService.initialIsAuthenticated,
    updateName(name: string): void {
        user.name = name;
    },
    updateAuthenticated(authenticated: boolean) {
        user.authenticated = authenticated;
    },
    updateUser(authenticated: boolean, username: string) {
        user.authenticated = authenticated;
        user.name = username;
    }
}, userActions);

const carEditionActions = {
    updateCar: action, updateCreationType: action, updateMaturityDate: action,updatePrice:action,
    updateModel: action, unselectCar: action, updateMaker: action, createNewCar:action
}
const carEdition = observable({
    creationType: false,
    car: null as Car,
    index: null as number,
    updateMaker(make: CarMaker.e) {
        carEdition.car.make = make;
    },
    updateModel(model: string) {
        carEdition.car.model = model;
    },
    updatePrice(price: string) {
        carEdition.car.price = Number(price);
    },
    updateMaturityDate(maturityDate: Date): void {
        carEdition.car.maturityDate = maturityDate;
    },
    updateCar(idx: number, car: Car): void {
        carEdition.creationType = false;
        carEdition.index = idx;
        carEdition.car = { ...car };
    },
    unselectCar(): void {
        carEdition.index = null;
        carEdition.car = null;
    },
    updateCreationType(creationType: boolean) {
        carEdition.creationType = creationType;
    },
    createNewCar(){
        carEdition.index = null;
        carEdition.creationType = true;
        carEdition.car = {
            make:null,
            maturityDate:new Date(),
            model:null,
            price:null,
            getPK(){
                return carEdition.car;
            }
        };
    },
    get isReadyToEdition(): Boolean {
        return carEdition.car !== null;
    },
    get title(): string {
        if (carEdition.creationType) {
            return "Create car";
        } else {
            return "Edit car";
        }
    },
}, carEditionActions);

window.addEventListener("popstate", e => navigationStateHandler(e.state));

const navigationStateHandler = state => {
    const current: Router = state === null ? window["__state"] : state;
    router.update(current.page);
};

const navigation = {
    gotoRegister() {
        const state = { page: "register" };
        navigationStateHandler(state);
        history.pushState(state, "register", "/register");
    },
    gotoLogin(): void {
        const state = { page: "login" };
        navigationStateHandler(state);
        history.pushState(state, "login", "/login");
    },
    loginOffEvent(): void {
        userService.userLogout().then(()=>{
            user.updateAuthenticated(false);
        });
    },
    root(): void {
        const state = { page: "" };
        navigationStateHandler(state);
        history.pushState(state, "", "/");
    }
}

export const stores = { router, carList, user, carEdition, navigation };


