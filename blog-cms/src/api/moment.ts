import {type ApiResponse, type PageInfo, request} from "@/plugins/axios2";
import type {Moment} from "@/types/momentType";

export function getMomentListByQuery(queryInfo: any): Promise<ApiResponse<PageInfo<Moment>>>{
    return request({
        url: 'moments',
        method: 'GET',
        params: {...queryInfo}
    })
}

export function updatePublished(id: number, published: boolean):Promise<ApiResponse<void>> {
    return request({
        url: 'moment/published',
        method: 'PUT',
        params: {
            id,
            published
        }
    })
}


export function getMomentById(id: number): Promise<ApiResponse<Moment>> {
    return request({
        url: 'moment',
        method: 'GET',
        params: {
            id
        }
    })
}

export function deleteMomentById(id: number): Promise<ApiResponse<void>> {
    return request({
        url: 'moment',
        method: 'DELETE',
        params: {
            id
        }
    })
}

export function saveMoment(moment:Moment): Promise<ApiResponse<Moment>> {
    return request({
        url: 'moment',
        method: 'POST',
        data: {
            ...moment
        }
    })
}

export function updateMoment(moment: Moment): Promise<ApiResponse<Moment>> {
    return request({
        url: 'moment',
        method: 'PUT',
        data: {
            ...moment
        }
    })
}