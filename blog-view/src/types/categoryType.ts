import type {PageResult} from '@/types/commonType'
import type {BlogInfo} from "@/types/blogType";

export interface Category {
    slug: string;
    name: string;
}
export interface CategoryGetBlogsResponse {
    categorySlug: Category,
    blogInfos: PageResult<BlogInfo>;
}