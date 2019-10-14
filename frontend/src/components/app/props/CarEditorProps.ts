import { uiStore } from "../../../stores/UIStore";
import { stores } from "../../../stores/Stores";

export namespace carEdit {
    export const props = {
        updateCar(e:React.FormEvent<any>):void{
            console.log("try to update");
            stores.carList.update(stores.carEdition.index,stores.carEdition.car);
            e.preventDefault();
        },
        onChangeMinutes(e: React.ChangeEvent<any>): void {
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
        }, onChangeMonth(e: React.ChangeEvent<any>): void {
            const nDate = new Date(stores.carEdition.car.maturityDate);
            nDate.setMonth(e.target.value);
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
    /*
    export interface MaturityDate {
        onChangeMinutes: (e: React.ChangeEvent<any>) => void;
        onChangeHour: (e: React.ChangeEvent<any>) => void;
        onChangeDay: (e: React.ChangeEvent<any>) => void;
        onChangeMonth: (e: React.ChangeEvent<any>) => void;
        onChangeYear: (e: React.ChangeEvent<any>) => void;
        onClickShowCalendar: (e: React.MouseEvent<any>) => void;
        onCalendarChange: (day: number) => void;
        calendarIconClasses: string;
        openedCalendar: boolean;
        value: Date;
    }


    export interface Props {
        onSubmit: (e: React.FormEvent<any>) => void
        title: string;
        maturityDate: MaturityDate;
    }

    export interface Controller {
        updateSelected(car: Car): void;
        props: Props;
    }
    export const carStore = observable({
        maturityDate: null as Date,
        updateMaturityDate(maturityDate: Date) {
            carStore.maturityDate = maturityDate;
        }
    }, {
            updateMaturityDate: action
        }
    );
    export function createController(): Controller {
        const store = observable({
            maturityDate: null as Date,
            selectedCar: null as Car,
            updateMaturityDate(maturityDate: Date) {
                console.log("new date" + maturityDate)
                store.maturityDate = maturityDate;
            },
            updateSelectedCar(selectedCar: Car) {
                store.selectedCar = selectedCar;
                store.maturityDate = selectedCar.maturityDate;
            }
        }, {
                updateSelectedCar: action,
                updateMaturityDate: action
            }
        );

        const props: Props = {
            maturityDate: {
                get calendarIconClasses() {
                    return "fa fa-calendar" + (props.maturityDate.openedCalendar ? "-day" : "");
                },
                onCalendarChange(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setDate(e);
                    store.updateMaturityDate(nDate);
                },
                onChangeYear(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setFullYear(e.target.value);
                    store.updateMaturityDate(nDate);
                },
                onChangeMonth(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setMonth(e.target.value);
                    store.updateMaturityDate(nDate);
                },
                onChangeHour(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setHours(e.target.value);
                    store.updateMaturityDate(nDate);
                },
                onChangeDay(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setDate(e.target.value);
                    store.updateMaturityDate(nDate);
                },
                onChangeMinutes(e) {
                    const nDate = new Date(store.maturityDate);
                    nDate.setMinutes(e.target.value);
                    store.updateMaturityDate(nDate);
                },
                onClickShowCalendar() {
                    uiStore.toggleCarEditCalendar();
                },
                get openedCalendar() {
                    return uiStore.carEditCalendarShow;
                },
                get value() {
                    return store.selectedCar === null
                        ? null
                        : store.maturityDate;
                }
            },
            onSubmit() { },
            title: "edit",
            get isSelected() {
                return store.selectedCar !== null;
            }
        }
        return {
            props,
            updateSelected: store.updateSelectedCar
        }
    }
       */
}