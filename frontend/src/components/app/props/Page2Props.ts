import { appInput } from "./AppInputProps";
import { dropDown } from "./DropDownProps";
import { Page2 } from "../../gen/Page2Tpl";
import * as React from "react";


export namespace page2 {
    const input1Store = appInput.createStore(null);
    const input2Store = appInput.createStore(null);
    const dd1Store = dropDown.createStore(0);

    export interface Props {
        input1: appInput.Props;
        input2: appInput.Props;
        dd1: dropDown.Props;
        submitForm: React.FormEventHandler<HTMLFormElement>
    }

    export function createProps(): Props {
        function checkDd1Error(this: dropDown.Props) {
            if (this.index === 0) {
                this.updateError("select a valid item");
            } else {
                this.updateError("");
            }
        }
        const dd1: dropDown.Props = dropDown.createProps({
            inputName: "dd1",
            items: ["(none)", "item 1", "item 2"],
            label: "dd exapmle",
            tabIndex: 2,
            store: dd1Store,
        });
        function mandatoryAppInput(this: appInput.Props): void {
            if ((this.value || "").length === 0) {
                this.updateError(`${this.label} is a mandatory value`);
            }
            else this.updateError("");
        }
        const input1: appInput.Props = appInput.createAppInputProps({
            labelId: "labelId1",
            inputType: appInput.Type.TEXT,
            label: "input1",
            store: input1Store,
            tabIndex: 1,
            placeholder: null,
        });
        const input2: appInput.Props = appInput.createAppInputProps({
            labelId: "labelId2",
            inputType: appInput.Type.TEXT,
            label: "input2",
            store: input2Store,
            tabIndex: 2,
            placeholder: null
        });
        function submitForm(e: React.FormEvent): void {
            mandatoryAppInput.bind(input1)();
            mandatoryAppInput.bind(input2)();
            checkDd1Error.bind(dd1)();
            e.preventDefault();
        }

        const props: Props = {
            dd1: dd1,
            input1: input1,
            input2: input2,
            submitForm: submitForm
        };
        return props;
    }

    export function createComponent(): React.FunctionComponentElement<Props> {
        return React.createElement(Page2, { ...createProps() });
    }
}