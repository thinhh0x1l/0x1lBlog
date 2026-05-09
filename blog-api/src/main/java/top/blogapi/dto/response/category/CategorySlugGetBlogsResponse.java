package top.blogapi.dto.response.category;


import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.blog.BlogInfo;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class CategorySlugGetBlogsResponse {
    CategorySlug categorySlug;
    PageInfo<BlogInfo> blogInfos;
}
