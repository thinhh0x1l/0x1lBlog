import {type ApiResponse, type PageResult, request} from "@/plugins/axios2";
import type {Moment} from "@/types/momentType";

export function getMomentListByPageNum(pageNum: number): Promise<ApiResponse<PageResult<Moment>>> {
    return request({
        url: 'moments',
        method: 'GET',
        params: {
            pageNum
        }
    })
}

export function likeMoment(id: number): Promise<ApiResponse<void>> {
    return request({
        url: 'moment/like',
        method: 'PUT',
        params: {
            id
        }
    })
}