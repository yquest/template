import axios from "axios";
import { User } from "../model/User";
import Axios from "axios";
import { UserService } from "./UserService";

export interface RestResult {
    data: any;
    status: number;
}

export class UserServiceImp implements UserService{
    private nameCached: string = (window["__state"] as AppInitialData).username;
    private authenticatedCache: boolean = (window["__state"] as AppInitialData).auth;
    get name ():string{
        return this.nameCached;
    }
    get authenticated(): boolean {
        return this.authenticatedCache;
    }
    get initialIsAuthenticated(): boolean {
        return this.authenticated;
    }
    async registerUser(user: User): Promise<void> {
        let body = {
            user: user.user,
            email: user.email,
            pass: user.pass
        };
        await Axios.post("api/user", body)
    };

    async userLogin(user: User): Promise<boolean> {
        this.authenticatedCache = await Axios.post("api/user/login", {
            user: user.user,
            pass: user.pass
        }).then(res=> res.status === 200).catch(res=> false);
        return this.authenticatedCache;
    };

    async userLogout(): Promise<void> {
        this.authenticatedCache = false;
        this.nameCached = null;
        await axios.get("/api/user/logout");
    };

    get initialName(): string {
        const initialData: AppInitialData = window["__state"];
        return initialData.username;
    }
}
