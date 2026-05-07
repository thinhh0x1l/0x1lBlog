import {request, type ApiResponse} from '@/plugins/axios2'
import type {Tag, TagIdGetBlogsResponse, TagSlug} from "@/types/tagType";



export const getTags = (): Promise<ApiResponse<TagSlug[]>> =>
    request({
        url: 'tags',
        method: 'get'
    })


export const getBlogListByTagId =
    (slug: string, pageNum: number, pageSize: number): Promise<ApiResponse<TagIdGetBlogsResponse>> =>
    request({
        url: `tag/${slug}`,
        method: 'GET',
        params: {
            pageNum,
            pageSize
        }
    })


