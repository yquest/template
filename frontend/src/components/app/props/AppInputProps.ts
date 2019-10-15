import { observable, action } from "mobx";
import classNames from "classnames/bind";

export namespace appInput {
    export enum Type {
        TEXT,
        PASSWORD,
        SELECT,
        DATE_TIME,
        NUMBER
    }

    export interface Props {
        onChange: React.ChangeEventHandler<HTMLInputElement>;
        inputType: Type;
        label: string;
        labelId: string;
        error: string;
        placeholder?: string;
        disabled?: boolean;
        tabIndex?: number;
        value: string;
        onBlur: (e) => void;
        errorClasses: string;
        onFocus: (e) => void;
        updateError(e: string);
    }

    export interface Entry {
        store: Store;
        disabled?: boolean;
        inputType: Type;
        label: string;
        labelId: string;
        placeholder: string | null;
        tabIndex: number;
    }

    export interface Store {
        value: string;
        error: string;
        updateValue(value: string): void;
        updateError(error: string): void;
    }

    export function createStore(value:string):Store{
        const store = observable(
            {
                value: value,
                error: null,
                updateValue(value: string) {
                    store.value = value;
                },
                updateError(error: string) {
                    store.error = error;
                }
            },
            {
                updateValue: action,
                updateError: action
            }
        );
        return store;
    }

    export function createAppInputProps(entry: Entry): Props {
        const props: Props = {
            updateError: entry.store.updateError,
            get error() { return entry.store.error; },
            get errorClasses() {
                return classNames({
                    "form-control": true,
                    "is-valid": props.error === "",
                    "is-invalid": (props.error || "").length > 0
                });
            },
            get disabled() { return entry.disabled; },
            onFocus(e) {
                if (props.inputType === Type.DATE_TIME) {
                    e.target.type = "datetime-local";
                }
            },
            onBlur(e) {
                if (
                    props.inputType === Type.DATE_TIME &&
                    props.value.length === 0
                ) {
                    e.target.type = "text";
                }
            },
            inputType: entry.inputType,
            label: entry.label,
            labelId: entry.labelId,
            placeholder: entry.placeholder,
            onChange(e) {
                entry.store.updateValue(e.target.value);
            },
            tabIndex: entry.tabIndex,
            get value() {
                return entry.store.value;
            }
        };
        return props;
    }
}