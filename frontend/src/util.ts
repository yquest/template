export const monthsListCondensed = ["jan","fev","mar","abr","mai","jun","jul","ago","set","out","nov","dec"];

export function pad(num: number, size: number): string {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}

export function dateToString(date: Date) {
    return `${pad(date.getFullYear(), 4)}-${pad(date.getMonth() + 1, 2)}-${pad(date.getDate(), 2)}T${pad(date.getHours(), 2)}:${pad(date.getMinutes(), 2)}:${pad(date.getSeconds(), 2)}Z`;
}

export function dateToStringReadable(date: Date) {
    return `${date.getFullYear()}-${pad(date.getMonth()+1, 2)}-${pad(
        date.getDate(),
        2
    )}, ${pad(date.getHours(), 2)}:${pad(date.getMinutes(), 2)}`;
}