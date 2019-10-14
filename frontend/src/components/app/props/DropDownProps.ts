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
        onSelectItem: (idx:number) => (e)=>void;
        itemClasses: string;
        classesIsValid: string;
        inputValue: string;
        inputName: string;
        error: string;
        index: number;
        updateError(error:string);
    }

    export interface Entry {
        items: string[];
        inputName: string;
        label: string;
        tabIndex: number;
        selectIndex: number;
    }
    export function createProps(entry: Entry): Props {
        const store = observable(
            {
                open: false,
                selectedIndex: entry.selectIndex,
                error:null as string,
                toggle() {
                    store.open = !store.open;
                },
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
                toggle: action,
                updateOpen: action,
                updateAndClose: action,
                updateSelectIndex: action,
                updateError:action
            }
        );
        const props:Props = {
            get index(){
                return store.selectedIndex;
            },
            blur() {
                store.updateOpen(false);
            },
            label: entry.label,
            tabIndex: entry.tabIndex,
            get classesIsOpen() {
                return classNames(
                    { "dropdown-menu": true },
                    { show: store.open }
                );
            },
            get classesIsValid() {
                return classNames(
                    { "form-control": true },
                    { "is-invalid": store.error != null && store.error.length > 0 },
                    { "is-valid": store.error != null && store.error.length === 0 }
                );
            },
            disabled: false,
            get error(){ return store.error},
            inputName: entry.inputName,
            get inputValue() {
                return props.labels[store.selectedIndex];
            },
            get itemClasses() {
                return classNames({
                    "dropdown-item": true,
                    selected: entry.selectIndex === store.selectedIndex
                });
            },
            keyDown(e) {
                switch (e.keyCode) {
                    case 38: //up key
                        if (entry.selectIndex > 0) {
                            store.updateSelectIndex(store.selectedIndex - 1);
                        }
                        e.preventDefault();
                        break;
                    case 40: //down key
                        if (entry.selectIndex < entry.items.length - 1) {
                            store.updateSelectIndex(store.selectedIndex + 1);
                        }
                        e.preventDefault();
                        break;
                }
            },
            labels: entry.items,
            togle: store.toggle,
            onSelectItem(idx) {
                return (e)=>{
                    store.updateAndClose(idx);
                    e.preventDefault();
                }
            },
            updateError:store.updateError
        }
        return props;
    }
}
