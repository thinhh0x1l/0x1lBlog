import {type ApiResponse, request} from "@/plugins/axios2";
import type {About} from "@/types/aboutType";

export function fAbout(): Promise<ApiResponse<About[]>>{
    return request({
        url:'about',
        method:'GET'
    })
}
export function fUpdateAbout(abouts: About[]): Promise<ApiResponse<void>>{
    return request({
        url:'about',
        method:'PUT',
        data:abouts

    })
}