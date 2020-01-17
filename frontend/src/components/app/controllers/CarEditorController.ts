import { uiStore } from "../../../stores/UIStore";
import { observable, action } from "mobx";
import { stores } from "../../../stores/Stores";
import { appInput } from "../../../controllers/AppInputController";
import { AppInput } from "../../gen/global/AppInputTpl";
import * as React from "react";
import { dropDown } from "../../../controllers/DropDownController";
import { DropDownInput } from "../../gen/global/DropDownTpl";
import { services } from "../../../services/Services";
import { CarMaker } from "../../../model/gen/CarMaker";


export namespace carEdit {
    const errorActions = { updateMake: action, updateModel: action, updatePrice: action, updateMaturityDate: action };
    const errors = observable({
        make: null as string,
        model: null as string,
        price: null as string,
        maturityDate: null as string,
        updateMake(make: string) {
            errors.make = make;
        },
        updateModel(model: string) {
            errors.model = model;
        },
        updatePrice(price: string) {
            errors.price = price;
        },
        updateMaturityDate(maturityDate: string) {
            errors.maturityDate = maturityDate;
        }
    }, errorActions);

    const makeStoreActions = { updateOpen: action };
    const makeStore = observable({
        open: false,
        updateOpen(open: boolean): void {
            makeStore.open = open;
        }
    }, makeStoreActions);

    export function createMakeInput(tabIndex: number): React.FunctionComponentElement<dropDown.Props> {

        const store: dropDown.Store = {
            error: errors.make,
            open: makeStore.open,
            get selectedIndex() {
                if (stores.carEdition.car.make === null) return 0;
                else return stores.carEdition.car.make + 1;
            },
            updateAndClose(selectedIndex) {
                store.updateSelectIndex(selectedIndex);
                makeStore.updateOpen(false);
            },
            updateError: errors.updateMake,
            updateOpen: makeStore.updateOpen,
            updateSelectIndex(selectedIndex) {
                if (selectedIndex === 0) stores.carEdition.updateMaker(null);
                else stores.carEdition.updateMaker(selectedIndex - 1);
            }
        };

        return React.createElement(DropDownInput, dropDown.createProps({
            disabled: !stores.carEdition.creationType,
            inputName: "make",
            items: ['(none)', ...CarMaker.labels()],
            label: "Maker",
            store: store,
            tabIndex: tabIndex
        }));
    }

    export function createModelInput(tabIndex: number): React.FunctionComponentElement<appInput.Props> {

        const modelStore: appInput.Store = {
            error: errors.model,
            get value() {
                return stores.carEdition.car.model;
            },
            updateValue(value) {
                stores.carEdition.updateModel(value)
            },
            updateError(error) {
                errors.updateModel(error);
            }
        };

        modelStore.updateValue(stores.carEdition.car.model);
        const props = appInput.createAppInputProps({
            disabled: !stores.carEdition.creationType,
            inputType: appInput.Type.TEXT,
            label: "Model",
            labelId: "model",
            placeholder: "model",
            store: modelStore,
            tabIndex: tabIndex
        });
        return React.createElement(AppInput, props);
    }

    export function createPriceInput(tabIndex: number): React.FunctionComponentElement<appInput.Props> {

        const priceStore: appInput.Store = {
            error: errors.price,
            get value() {
                if (stores.carEdition.car.price === null) return "";
                return stores.carEdition.car.price.toString();
            },
            updateValue(value) {
                stores.carEdition.updatePrice(value)
            },
            updateError(error) {
                errors.updatePrice(error);
            }
        };

        if (stores.carEdition.car.price === null) {
            priceStore.updateValue("0");
        } else {
            priceStore.updateValue(stores.carEdition.car.price.toString());
        }
        const props = appInput.createAppInputProps({
            disabled: false,
            inputType: appInput.Type.NUMBER,
            label: "Price",
            labelId: "price",
            placeholder: "price",
            store: priceStore,
            tabIndex: tabIndex
        });
        return React.createElement(AppInput, props);
    }

    export const props = {
        onCancel(e: React.MouseEvent<HTMLButtonElement>) {
            stores.carEdition.unselectCar();
            e.preventDefault();
        },
        updateCar(e: React.FormEvent<any>): void {
            let hasErrors = false;
            if ((stores.carEdition.car.model || "").length === 0) {
                errors.updateModel("should be filled");
                hasErrors = true;
            } else {
                errors.updateModel("")
            }
            if (stores.carEdition.car.make === null) {
                errors.updateMake("should be filled");
                hasErrors = true;
            } else {
                errors.updateMake("")
            }
            if (stores.carEdition.car.price <= 0) {
                errors.updatePrice("should be greater than 0");
                hasErrors = true;
            } else {
                errors.updatePrice("")
            }
            if (!hasErrors) {
                const carToUpadate = {...stores.carEdition.car};
                const carIndex = stores.carEdition.index;
                if (stores.carEdition.creationType) { 
                    services.carService.create(carToUpadate).then(res=>{
                        stores.carList.createCar(carToUpadate); 
                    });
                }
                else {
                    services.carService.update(carToUpadate).then(res=>{
                        console.log("index",carIndex)
                        stores.carList.update(carIndex, carToUpadate);
                    });
                }
                errors.updateModel(null);
                errors.updateMake(null);
                errors.updatePrice(null);
                stores.carEdition.unselectCar();
            }
            e.preventDefault();
        }, onChangeMinutes(e: React.ChangeEvent<any>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setMinutes(e.target.value);
            stores.carEdition.updateMaturityDate(nDate);
        }, onChangeHour(e: React.ChangeEvent<any>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setHours(e.target.value);
            stores.carEdition.updateMaturityDate(nDate);
        }, onChangeDay(e: React.ChangeEvent<any>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setDate(e.target.value);
            stores.carEdition.updateMaturityDate(nDate);
        }, onChangeMonth(e: React.ChangeEvent<HTMLSelectElement>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setMonth(e.target.selectedIndex);
            stores.carEdition.updateMaturityDate(nDate);
        }, onChangeYear(e: React.ChangeEvent<any>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setFullYear(e.target.value);
            stores.carEdition.updateMaturityDate(nDate);
        }, onClickShowCalendar(_: React.MouseEvent<any>): void {
            uiStore.toggleCarEditCalendar();
        }, onCalendarChange(day: number): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setDate(day);
            stores.carEdition.updateMaturityDate(nDate);
        }, get calendarIconClasses(): string {
            return "fa fa-calendar" + (uiStore.carEditCalendarShow ? "-day" : "")
        }, get openCalendar(): boolean {
            return uiStore.carEditCalendarShow;
        }
    }

}