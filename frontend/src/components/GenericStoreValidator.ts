import { observable, action } from "mobx";

export interface ValidatedElement {
        value: any,
        error: string
}
export interface GenericStore {
    values: ValidatedElement[];
    update(idx: number, value: any);
    checkAllErrors();
    isAllValidated: boolean;
    reset(): void;
}

export const createGenericStore = (max: number, create: (idx: number)=> any, validation: (idx: number, value: any) => string,reset?: (idx: number, value: any) => ValidatedElement): GenericStore => {
    let newValues = [];
    for (let i = 0; i < max; i++) {
        newValues[i] = {
            value: create(i),
            error: null
        };
    }
    return observable.object(
        {
            values: newValues,
            get isAllValidated(): boolean {
                for (let i = 0; i < max; i++) {
                    let current = this.values[i];
                    let error: string = current.error === null ? validation(i, current.value) : current.error;
                    if (error !== null && error.length > 0) {
                        return false;
                    }
                }
                return true;
            },
            reset(): void { 
                let resetByIndex = reset===undefined?(idx:number, value:any)=>{
                    return this.values[idx] = {
                        value:create(idx),
                        error:null
                    };
                }:reset;
                for (let i = 0; i < max; i++) {
                    resetByIndex(i,this.values[i]);
                }
            },
            update(idx: number, value: any): void {
                this.values[idx] = { value, error: validation(idx, value) };
            },
            checkAllErrors(): void {
                for (let i = 0; i < max; i++) {
                    let current = this.values[i];
                    let error = current.error === null ? validation(i, current.value) : current.error;
                    current.error = error;
                }
            }
        },
        {
            update: action,
            checkAllErrors: action,
            reset: action
        }
    );
};