import type {PageResult} from '@/types/commonType'
import type {BlogInfo} from "@/types/blogType";

export interface Tag{
    id: number,
    name: string,
    color: string
}
export interface TagSlug{
    slug: string,
    name: string,
    color: string
}
export interface TagIdGetBlogsResponse {
    queryTag: TagSlug,
    blogInfos: PageResult<BlogInfo>;
}