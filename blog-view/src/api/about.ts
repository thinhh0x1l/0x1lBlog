import {request} from "@/plugins/axios2";

import type {ApiResponse} from "@/plugins/axios2";
import type {About} from "@/types/aboutType";


export function fGetAboutList(): Promise<ApiResponse<About>> {
    return request({
        url: 'about',
        method: 'GET'
    })
}