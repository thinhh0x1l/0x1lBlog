package top.blogapi.dto.response.category;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response._page.PageResult;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CategorySlugGetBlogsResponse {
    CategorySlug categorySlug;
    PageResult<BlogInfo> blogInfos;
}
