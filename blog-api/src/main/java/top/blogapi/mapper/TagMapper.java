package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response.tag.TagSlugGetBlogsResponse;
import top.blogapi.dto.response.tag.TagResponse;
import top.blogapi.dto.response.tag.TagSlugs;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.util.SlugUtils;
import top.blogapi.util.StringUtils;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    @Mappings({
            @Mapping(source = "id", target = "tagId"),
            @Mapping(source = "name", target = "tagName"),
            @Mapping(source = "color", target = "tagColor")
    })
    TagResponse tagToTagResponse(Tag tag);

    TagSlugs toTagSlugs(Tag tag, String slug);

    List<BlogInfo> toTagIdGetBlogInfoList(List<BlogTagsInfo> blogTagsInfos);

}
