import { UserService } from "../services/UsrService";
import { User } from "../model/User";

export class UserServiceTest implements UserService{
    private usersList:User[] = [];
    private nameCached: string = (window["__state"] as AppInitialData).username;
    private authenticatedCache: boolean = (window["__state"] as AppInitialData).auth;
    
    get name ():string{
        return this.nameCached;
    }
    get authenticated(): boolean {
        return this.authenticatedCache;
    }
    
    async registerUser(user: User): Promise<void> {
        this.usersList.push(user);
        return Promise.resolve();
    }
    async userLogin(user: User): Promise<boolean> {
        function findUser(current:User):boolean{
            return current.user === user.user && current.pass === user.pass;
        }
        this.authenticatedCache = this.usersList.findIndex(findUser) !== -1;
        return Promise.resolve(this.authenticatedCache)
    }
}