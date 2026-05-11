import {request} from "@/plugins/axios2";

import type {About} from "@/types/aboutType";
import type {ApiResponse} from "@/types/commonType";


export function fGetAboutList(): Promise<ApiResponse<About>> {
    return request({
        url: 'about',
        method: 'GET'
    })
}