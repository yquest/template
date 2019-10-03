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
        onSelectItem:(idx:number)=>(e)=>void;
        itemClasses:(idx:number)=>string;
        classesIsValid:string;
        inputValue:string;
        inputName:string;
        error:string;
    }
}
