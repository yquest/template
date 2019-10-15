import { observable, action } from "mobx";
import classNames from "classnames/bind";

export namespace dropDown {
    export interface Props {
        label: string;
        tabIndex: number;
        disabled: boolean;
        keyDown: (e) => void;
        togle: (e) => void;
        blur: (e: React.FocusEvent) => void;
        classesIsOpen: string;
        labels: string[];
        onSelectItem: (idx: number) => (e) => void;
        itemClasses: (idx: number) => string;
        classesIsValid: string;
        inputValue: string;
        inputName: string;
        error: string;
        index: number;
        updateError(error: string);
    }

    export interface Entry {
        items: string[];
        inputName: string;
        label: string;
        tabIndex: number;
        store: Store;
    }

    export interface Store {
        updateAndClose(idx: number): void;
        open: boolean;
        selectedIndex: number;
        error: string;
        updateOpen(open: boolean): void;
        updateSelectIndex(idx: number): void;
        updateError(error: string): void;
    }
    export function createStore(selectedIndex:number):Store{
        const store = observable(
            {
                open: false,
                selectedIndex: selectedIndex,
                error:null as string,
                updateOpen(open: boolean) {
                    store.open = open;
                },
                updateSelectIndex(idx: number) {
                    store.selectedIndex = idx;
                },
                updateAndClose(idx: number) {
                    store.open = false;
                    store.selectedIndex = idx;
                },
                updateError(error:string){
                    store.error = error;
                }
            },
            {
                updateOpen: action,
                updateAndClose: action,
                updateSelectIndex: action,
                updateError:action
            }
        );
        return store;
    }
    export function createProps(entry: Entry): Props {
        const props: Props = {
            get index() {
                return entry.store.selectedIndex;
            },
            blur() {
                entry.store.updateOpen(false);
            },
            label: entry.label,
            tabIndex: entry.tabIndex,
            get classesIsOpen() {
                return classNames(
                    { "dropdown-menu": true },
                    { show: entry.store.open }
                );
            },
            get classesIsValid() {
                return classNames(
                    { "form-control": true },
                    { "is-invalid": entry.store.error != null && entry.store.error.length > 0 },
                    { "is-valid": entry.store.error != null && entry.store.error.length === 0 }
                );
            },
            disabled: false,
            get error() { return entry.store.error },
            inputName: entry.inputName,
            get inputValue() {
                return props.labels[entry.store.selectedIndex];
            },
            itemClasses(idx: number) {
                return classNames({
                    "dropdown-item": true,
                    selected: entry.store.selectedIndex === idx
                });
            },
            keyDown(e) {
                switch (e.keyCode) {
                    case 38: //up key
                        if (entry.store.selectedIndex > 0) {
                            entry.store.updateSelectIndex(entry.store.selectedIndex - 1);
                        }
                        e.preventDefault();
                        break;
                    case 40: //down key
                        if (entry.store.selectedIndex < entry.items.length - 1) {
                            entry.store.updateSelectIndex(entry.store.selectedIndex + 1);
                        }
                        e.preventDefault();
                        break;
                }
            },
            labels: entry.items,
            togle() { entry.store.updateOpen(!entry.store.open) },
            onSelectItem(idx) {
                return (e) => {
                    entry.store.updateAndClose(idx);
                    e.preventDefault();
                }
            },
            updateError: entry.store.updateError
        }
        return props;
    }
}
