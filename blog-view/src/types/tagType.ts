import type {PageInfo} from "@/plugins/axios2";
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
    blogInfos: PageInfo<BlogInfo>;
}