import { UserService } from "../services/UserService";
import { User } from "../model/User";

export class UserServiceTest implements UserService{
    private usersList:User[] = [];
    private nameCached: string;
    private authenticatedCache: boolean;
    constructor(){
        const usersStorage = localStorage.getItem("users");
        const initialData = usersStorage != null? JSON.parse(localStorage.getItem("users")):[];
        if(initialData == null){
            this.nameCached = null;
            this.authenticatedCache = false;
        }
        const state = window['__state'] || {};
        state.page = null;
        window['__state'] = state;
    }
    userLogout(): Promise<void> {
        this.nameCached = null;
        this.authenticatedCache = false;
        return Promise.resolve()       
    }
    
    get name ():string{
        return this.nameCached;
    }
    get authenticated(): boolean {
        return this.authenticatedCache;
    }
    
    registerUser(user: User): Promise<void> {
        this.usersList.push(user);
        return Promise.resolve();
    }
    userLogin(user: User): Promise<boolean> {
        function findUser(current:User):boolean{
            return current.user === user.user && current.pass === user.pass;
        }
        this.authenticatedCache = this.usersList.findIndex(findUser) !== -1;
        return Promise.resolve(this.authenticatedCache)
    }
}