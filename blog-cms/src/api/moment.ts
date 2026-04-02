import {type ApiResponse, type PageInfo, request} from "@/plugins/axios2";
import type {Moment} from "@/types/momentType";

export function getMomentListByQuery(queryInfo: any): Promise<ApiResponse<PageInfo<Moment>>>{
    return request({
        url: 'moments',
        method: 'GET',
        params: {...queryInfo}
    })
}

export function updatePublished(id: number, published: boolean):Promise<ApiResponse<null>> {
    return request({
        url: 'moment/published',
        method: 'PUT',
        params: {
            id,
            published
        }
    })
}