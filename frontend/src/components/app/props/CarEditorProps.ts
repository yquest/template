import { uiStore } from "../../../stores/UIStore";
import { observable, action } from "mobx";
import { stores } from "../../../stores/Stores";
import { appInput } from "./AppInputProps";
import { AppInput } from "../../gen/AppInputTpl";
import * as React from "react";
import { makerToString } from "../../../model/Car";
import { dropDown } from "./DropDownProps";
import { DropDownInput } from "../../gen/DropDownTpl";


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

    const dd1StoreActions = {updateOpen:action};
    const dd1Store = observable({
        open: false,
        updateOpen(open: boolean): void {
            dd1Store.open = open;
        }
    }, dd1StoreActions);

    export function createMakeInput(): React.FunctionComponentElement<dropDown.Props> {

        const store: dropDown.Store = {
            error: errors.make,
            open: dd1Store.open,
            get selectedIndex(){
                if (stores.carEdition.car.make === null) return 0;
                else return stores.carEdition.car.make +1;
            },
            updateAndClose(selectedIndex){
                store.updateSelectIndex(selectedIndex);
                dd1Store.updateOpen(false);
            },
            updateError: errors.updateMake,
            updateOpen: dd1Store.updateOpen,
            updateSelectIndex(selectedIndex){
                if(selectedIndex === 0) stores.carEdition.updateMaker(null);
                else stores.carEdition.updateMaker(selectedIndex-1);
            }
        };

        return React.createElement(DropDownInput, dropDown.createProps({
            inputName: "make",
            items: ['(none)',...Array.from(makerToString.values())],
            label: "Maker",
            store: store,
            tabIndex: 3
        }));
    }

    export function createModelInput(): React.FunctionComponentElement<appInput.Props> {

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
            disabled: false,
            inputType: appInput.Type.TEXT,
            label: "Model",
            labelId: "model",
            placeholder: "model",
            store: modelStore,
            tabIndex: 3
        });
        return React.createElement(AppInput, props);
    }
    export const props = {
        updateCar(e: React.FormEvent<any>): void {
            let hasErrors = false;
            if ((stores.carEdition.car.model || "").length === 0) {
                errors.updateModel("should be filled");
                hasErrors = true;
            } else {
                errors.updateModel("")
            }
            if(stores.carEdition.car.make === null){
                errors.updateMake("should be filled");
                hasErrors = true;
            }else {
                errors.updateMake("")
            }
            if (!hasErrors) {
                stores.carList.update(stores.carEdition.index, stores.carEdition.car);
                errors.updateModel(null);
                errors.updateMake(null);
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