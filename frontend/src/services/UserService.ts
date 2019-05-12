import axios from "axios";
import { User } from "../model/User";
import Axios from "axios";

export interface RestResult {
    data: any;
    status: number;
}

export class UserService {
    registerUser(user: User): Promise<RestResult> {
        let body = {
            name: user.username,
            email: user.email,
            password: user.password
        };
        return Axios.post("api/user", body)
    };
    userLogin(user: User): Promise<RestResult> {
        let body = {
            user: user.username,
            pass: user.password
        };
        return Axios.post("api/user/login", body);
    };

    userLogout(): Promise<any> {
        return axios.get("/api/user/logout");
    };

}

export const userService = new UserService();