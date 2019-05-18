export function pad(num:number, size:number):string {
    var s = num+"";
    while (s.length < size) s = "0" + s;
    return s;
}

export function dateToString(date:Date){
    return `${pad(date.getFullYear(),4)}-${pad(date.getMonth(),2)}-${pad(date.getUTCDay(),2)}T${pad(date.getUTCHours(),2)}:${pad(date.getUTCMinutes(),2)}:${pad(date.getSeconds(),2)}Z`;
}
