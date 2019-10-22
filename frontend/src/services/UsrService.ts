import { User } from "../model/User";

export interface UserService {
    authenticated:boolean;
    name:string;
    registerUser(user:User):Promise<void>
    userLogin(user:User):Promise<boolean>
}