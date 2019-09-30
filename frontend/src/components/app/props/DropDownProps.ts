export namespace dropDown {
    export interface Props {
        label:string;
        tabIndex:number;
        disabled:boolean;
        keyDown:(e)=>void;
        togle:(e)=>void;
        blur:(e)=>void;
        classesIsOpen:string;
        labels:string[];
        onSelectItem:(e)=>void;
        itemClasses:string;
        classesIsValid:string;
        inputValue:string;
        inputName:string;
        error:string;
    }
}
