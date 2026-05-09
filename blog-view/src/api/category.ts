import {type ApiResponse, request} from '@/plugins/axios2'
import type {Category, CategoryGetBlogsResponse} from "@/types/categoryType";

export function fGetCategoryList(): Promise<ApiResponse<Category[]>> {
    return request({
        url: 'categories',
        method: 'GET'
    })
}

export function fGetBlogListByCategoryName(slug: string,pageNum: number, pageSize: number): Promise<ApiResponse<CategoryGetBlogsResponse>> {
    return request({
        url: `category/${slug}`,
        method: 'GET',
        params: {
            pageNum,
            pageSize
        }
    })
}