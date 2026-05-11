import axios from '@/plugins/axios'
import type {CommentByBlogIdResponse, SaveCommentReq} from "@/types/commentType";
import type {ApiResponse} from "@/types/commonType";

export function getCommentListByQuery(query: any):Promise<ApiResponse<CommentByBlogIdResponse>> {
    return axios({
        url: 'comment-tree',
        method: 'GET',
        params: {
            ...query
        }
    })
}

export function submitComment(req: SaveCommentReq, token: string):Promise<ApiResponse<null>>{
    return axios({
        url: 'comment',
        method: 'POST',
        headers: {
            "Authorization": token
        },
        data: req,
    })
}