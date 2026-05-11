import type {Login, LoginRes} from "@/types/loginType";
import type {ApiResponse} from "@/types/commonType";
import {request} from "@/plugins/axios2";

export function loginAuth(loginInfo: Login): Promise<ApiResponse<LoginRes>>{
    return request({
        url: 'login',
        method: 'POST',
        data: loginInfo
    })
}