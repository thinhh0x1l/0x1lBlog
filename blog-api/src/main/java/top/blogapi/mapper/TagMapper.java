package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import top.blogapi.dto.response.tag.TagSlugGetBlogsResponse;
import top.blogapi.dto.response.tag.TagResponse;
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

    TagSlugGetBlogsResponse.Tag toTagIdGetBlogsResponse_Tag(Tag tag, String slug);

    List<TagSlugGetBlogsResponse.BlogInfo> toTagIdGetBlogInfoList(List<BlogTagsInfo> blogTagsInfos);

    @Mapping(target = "tags", source = ".", qualifiedByName = "convertTagToList")
    @Mapping(target = "description", source = "description", qualifiedByName = "convertMarkdownToHtml")
    TagSlugGetBlogsResponse.BlogInfo toTagIdGetBlogsResponse (BlogTagsInfo blogTagsInfo);

    @Named("convertMarkdownToHtml")
    default String convertMarkdownToHtml(String description) {
        if (StringUtils.isEmpty(description))
            return "";
        return MarkdownUtils.markdownToHtmlExtensions(description);
    }

    @Named("convertTagToList")
    default List<TagSlugGetBlogsResponse.Tag> convertToTagList(BlogTagsInfo source){
        if (source.getAllTagNames() == null || source.getAllTagNames().isEmpty())
            return List.of();

        String[] names = source.getAllTagNames().split(",");
        String[] colors = source.getAllTagColors().split(",");

        List<TagSlugGetBlogsResponse.Tag> tags = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            TagSlugGetBlogsResponse.Tag tag = new TagSlugGetBlogsResponse.Tag();
            tag.setSlug(SlugUtils.convertSpaceToHyphen(names[i]));
            tag.setName(names[i].trim());
            tag.setColor(colors[i].trim());
            tags.add(tag);
        }
        return tags;
    }

}
