import { observable, action } from "mobx";

export enum Control {
    INPUT1, INPUT2
}
const initialState = window['__state']
export const form1 = observable({
    input: {
        [Control.INPUT1]: initialState.input1,
        [Control.INPUT2]: initialState.input2
    },
    error: {
        [Control.INPUT1]: null,
        [Control.INPUT2]: null
    },
    updateError(index: number, value: string) {
        form1.error[index] = value;
    },
    updateValue(index: number, value: string) {
        form1.input[index] = value;
    }
}, {
        updateValue: action,
        updateError: action
    });

export function updateValue(index: Control): (value: string) => void {
    return (value) => {
        form1.updateValue(index, value);
    }
}

export function submitFormEvent(e: React.FormEvent<HTMLFormElement>) {
    let isValid = true;
    [Control.INPUT1, Control.INPUT2].forEach((i) => {
        if (form1.input[i].length === 0) {
            form1.updateError(i, "should be filled");
            isValid = false; 
        } else {
            form1.updateError(i, "");
        }
    });
    
    if(isValid){
        console.log("form valid");
    }else{
        console.log("form invalid");
    }

    e.preventDefault();
}