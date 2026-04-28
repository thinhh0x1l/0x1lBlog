import type {Login, LoginRes} from "@/types/loginType";
import {type ApiResponse, request} from "@/plugins/axios2";

export function loginAuth(loginInfo: Login): Promise<ApiResponse<LoginRes>>{
    return request({
        url: 'login',
        method: 'POST',
        data: loginInfo
    })
}