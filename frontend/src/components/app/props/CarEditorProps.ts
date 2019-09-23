export namespace carEdit {
    export interface MaturityDate{    
        onChangeMinutes:(e)=>void;
        onChangeHour:(e)=>void;
        onChangeDay:(e)=>void;
        onChangeMonth:(e)=>void;
        onChangeYear:(e)=>void;
        showCalendar:(e)=>void;
        onCalendarChange:(day:number)=>void;
        calendarIconClasses:string;
        openedCalendar:boolean;
        value:Date;
    }
    export interface Props {
        onSubmit: (e: React.FormEvent<HTMLFormElement>) => void
        title: string;
        maturityDate:MaturityDate;
    }
}