import { request} from "@/plugins/axios2";
import type {Moment, MomentLikeById, MomentLikedByGuestId} from "@/types/momentType";
import type {ApiResponse, PageResult} from "@/types/commonType";

export function getMomentListByPageNum(pageNum: number): Promise<ApiResponse<PageResult<MomentLikedByGuestId>>> {
    return request({
        url: 'moments',
        method: 'GET',
        params: {
            pageNum
        }
    })
}


export function toggleLikeApi(momentLikeById: MomentLikeById): Promise<ApiResponse<void>>{
    return request({
        url:'moment/like',
        method: 'PUT',
        data: momentLikeById
    })
}