package top.blogapi.dto.response._page;


import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.category.CategoryResponse;
import top.blogapi.dto.response.category.CategorySlug;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlogListPageResponse {
    PageInfo<BlogSummaryResponse> blogs;
    List<CategorySlug> categories;
}
