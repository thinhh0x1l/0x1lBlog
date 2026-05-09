import type {PageInfo} from "@/plugins/axios2";
import type {BlogInfo} from "@/types/blogType";

export interface Category {
    slug: string;
    name: string;
}
export interface CategoryGetBlogsResponse {
    categorySlug: Category,
    blogInfos: PageInfo<BlogInfo>;
}