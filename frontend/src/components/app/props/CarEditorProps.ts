import { uiStore } from "../../../stores/UIStore";
import { observable, action } from "mobx";
import { stores } from "../../../stores/Stores";
import { appInput } from "./AppInputProps";
import { AppInput } from "../../gen/AppInputTpl";
import * as React from "react";
import { SelectInputProps, SelectInput } from "../../tpl/SelectInput";
import { MAKERS, makerToString } from "../../../model/Car";


export namespace carEdit {
    const errorActions = { updateMake: action, updateModel: action, updatePrice: action, updateMaturityDate: action };
    export const errors = observable({
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

    export function createMakeInput():React.FunctionComponentElement<SelectInputProps>{

        const makeStore: SelectInputProps = {
            list: ['(none)',...makerToString.values()],
            toKey(key){
                return `maker-${key}`;
            },
            onChange(e){
                if(e.target.selectedIndex === 0){
                    stores.carEdition.updateMaker(null);
                }else{
                    stores.carEdition.updateMaker(e.target.selectedIndex-1);
                }                
            },
            get selected():number{
                console.log(`make:${stores.carEdition.car.make}`);
                if(stores.carEdition.car.make === null)return 0;
                else return stores.carEdition.car.make +1;
            }
        };

        console.log(makeStore.list);

        return React.createElement(SelectInput, makeStore);
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
            if(!hasErrors){
                stores.carList.update(stores.carEdition.index, stores.carEdition.car);
                errors.updateModel(null);
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