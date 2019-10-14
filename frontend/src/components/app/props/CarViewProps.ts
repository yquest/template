import { Car, MAKERS } from "../../../model/Car";
import { dateToStringReadable } from "../../../util";
import * as React from "react";
import { CarView } from "../../gen/CarViewTpl";
import { stores } from "../../../stores/Stores";


export namespace carView {
    export interface Updater {
        update: (car: Car) => void;
        remove: (car: Car) => void;
    }
    export interface Props {
        maker: string;
        model: string;
        maturityDate: string;
        price: string;
        authenticated: boolean;
        onEdit: (e: React.MouseEvent<HTMLAnchorElement>) => void;
        onRemove: (e: React.MouseEvent<HTMLAnchorElement>) => void;
    }
    export interface Controller {
        car: Car;
        props: Props;
    }
    export interface Entry {
        key: string;
        authenticated: boolean;
        car: Car;
        edit(): void;
        remove(): void;
    }
    export function createController(entry: Entry): Controller {
        const props: Props = {
            authenticated: entry.authenticated,
            maker: MAKERS[entry.car.make],
            maturityDate: dateToStringReadable(entry.car.maturityDate),
            model: entry.car.model,
            price: entry.car.price + "€",
            onEdit(e) {
                entry.edit();
                e.preventDefault();
            },
            onRemove(e) {
                entry.remove();
                e.preventDefault();
            }
        };

        return {
            props,
            car: entry.car
        };
    }
    export function createComponent(key: string, props: Props): React.FunctionComponentElement<Props> {
        return React.createElement(CarView, { ...props, ...{ key } });
    }
    export function carViewList(): React.FunctionComponentElement<Props>[] {
        return stores.carList.cars.map((car, idx) => {
            const carViewEntry: Entry = {
                key: idx.toString(),
                car: car,
                authenticated: stores.user.authenticated,
                edit() {
                    stores.carEdition.updateCar(idx,carViewEntry.car)
                },
                remove() {
                    stores.carList.remove(carViewEntry.car);
                }
            };
            const carViewController = carView.createController(carViewEntry);
            return createComponent(idx.toString(),carViewController.props);
        });
    }
}
