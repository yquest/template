import { Car, MAKERS, makerToString } from "../../../model/Car";
import { dateToStringReadable } from "../../../util";
import * as React from "react";
import { CarView } from "../../gen/CarViewTpl";
import { stores } from "../../../stores/Stores";
import { uiStore, showModal } from "../../tpl/ModalTpl";
import { ModalContent } from "../../../stores/UIStore";


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
        blockedRemove: boolean;
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
            get blockedRemove(){
                return idx === stores.carEdition.index;
            },
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
                    if(idx === stores.carEdition.index){
                        return;                    }
                    const modalContent:ModalContent = new ModalContent();
                    modalContent.actionButton = "remove";
                    modalContent.content = `Do you want to remove the car: (Model:${car.model}) Maker(${makerToString.get(car.make)})`;
                    modalContent.title = "Alert";
                    modalContent.actionEvent = ()=>{
                        stores.carList.remove(carViewEntry.car);
                    };
                    uiStore.updateModalContent(modalContent);
                    showModal();
                }
            };
            const props = carView.createProps(idx, carViewEntry);
            return createComponent(`car-view-${idx}`, props);
        });
    }
}
