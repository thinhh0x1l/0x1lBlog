package top.blogapi.dto.response.tag;

import com.github.pagehelper.PageInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;
import top.blogapi.dto.response.blog.BlogInfo;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagSlugGetBlogsResponse {
    TagSlugs queryTag;
    PageInfo<BlogInfo> blogInfos;
}


