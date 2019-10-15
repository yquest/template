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
        classes: string;
        maker: string;
        model: string;
        maturityDate: string;
        price: string;
        authenticated: boolean;
        onEdit: (e: React.MouseEvent<HTMLAnchorElement>) => void;
        onRemove: (e: React.MouseEvent<HTMLAnchorElement>) => void;
    }
    export interface Entry {
        key: string;
        authenticated: boolean;
        car: Car;
        edit(): void;
        remove(): void;
    }
    export function createProps(idx: number, entry: Entry): Props {
        const props: Props = {
            get classes(): string {
                return idx === stores.carEdition.index ? "selected" : null;
            },
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
        return props;
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
                    stores.carEdition.updateCar(idx, carViewEntry.car)
                },
                remove() {
                    stores.carList.remove(carViewEntry.car);
                }
            };
            const props = carView.createProps(idx, carViewEntry);
            return createComponent(idx.toString(), props);
        });
    }
}
