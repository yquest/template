export interface CarView2Props{
    maker: string;
    model: string;
    maturityDate: string;
    price: string;
    authenticated:boolean;
    carEdit:(e)=>void;
    remove:(e)=>void;
}