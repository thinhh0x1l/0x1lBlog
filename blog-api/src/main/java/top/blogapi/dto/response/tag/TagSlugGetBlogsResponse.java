package top.blogapi.dto.response.tag;

import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response._page.PageResult;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagSlugGetBlogsResponse {
    TagSlugs queryTag;
    PageResult<BlogInfo> blogInfos;
}


