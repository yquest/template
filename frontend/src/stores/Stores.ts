import { observable, IObservableArray, action, configure } from "mobx";
import { Car, MAKERS } from "../model/Car";
import { carService } from "../services/CarService";
import { userService } from "../services/UserService";

export namespace stores {
    configure({ enforceActions: "observed" });

    const routerActions = { update: action };
    export const router = observable({
        page: window["__state"]["page"] as string,
        update(page: string): void {
            router.page = page;
        }, get isRootPage(): Boolean {
            return (router.page || "/") == "/";
        }
    }, routerActions);

    const carListActions = { remove: action, update: action, updateCars: action };
    export const carList = observable({
        cars: carService.initialList as IObservableArray<Car>,
        remove(car: Car) {
            carList.cars.remove(car)
        },
        updateCars(cars: Car[]) {
            carList.cars.replace(cars);
        },
        update(idx: number, car: Car): void {
            carList.cars[idx] = { ...car };
        },
    }, carListActions);

    const userActions = { updateName: action, updateAuthenticated: action };
    export const user = observable({
        name: userService.initialName,
        authenticated: userService.initialIsAuthenticated,
        updateName(name: string): void {
            user.name = name;
        },
        updateAuthenticated(authenticated: boolean) {
            user.authenticated = authenticated;
        }
    }, userActions);

    const carEditionActions = { updateCar: action, updateCreationType: action, updateMaturityDate: action, 
        updateModel:action, unselectCar:action , updateMaker:action}
    export const carEdition = observable({
        creationType: false,
        car: null as Car,
        index: null as number,
        updateMaker(make: MAKERS) {
            carEdition.car.make = make;
        },
        updateModel(model: string) {
            carEdition.car.model = model;
        },
        updateMaturityDate(maturityDate: Date): void {
            carEdition.car.maturityDate = maturityDate;
        },
        updateCar(idx: number, car: Car): void {
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

    export const navigation = {
        gotoPage2(e: React.MouseEvent<any>): void {
            const state = { page: "page2" };
            navigationStateHandler(state);
            history.pushState(state, "page 2", "/page2");
            e.preventDefault();
        },
        loginOffEvent(e: React.MouseEvent<any>): void {
            user.updateAuthenticated(false);
            e.preventDefault();
        },
        loginPage(e: React.MouseEvent<any>): void {
            console.log('login page is not implemented yet');
            e.preventDefault();
        },
        root(e: React.MouseEvent<any>): void {
            const state = { page: "" };
            navigationStateHandler(state);
            history.pushState(state, "", "/");
            e.preventDefault();
        }
    }
}


