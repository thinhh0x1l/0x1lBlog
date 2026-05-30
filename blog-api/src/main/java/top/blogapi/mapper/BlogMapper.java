package top.blogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import top.blogapi.dto.response.blog.ArchiveBlogResponse;
import top.blogapi.dto.response.blog.BlogInfo;
import top.blogapi.dto.response.blog.BlogSummaryResponse;
import top.blogapi.dto.response.tag.TagSlugs;
import top.blogapi.model.entity.Blog;
import top.blogapi.dto.internal.ArchiveBlogInternal;
import top.blogapi.dto.internal.BlogTagsInfoInternal;
import top.blogapi.util.SlugUtils;
import top.blogapi.util.StringUtils;
import top.blogapi.util.markdown.MarkdownUtils;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(source = "category.name", target = "categoryName")
    BlogSummaryResponse toBlogSummaryResponse(Blog blog);


    ArchiveBlogResponse toArchiveBlogResponse(ArchiveBlogInternal archiveBlogInternal);

    @Mapping(target = "tags", source = ".", qualifiedByName = "convertTagToList")
    @Mapping(target = "description", source = "description", qualifiedByName = "convertMarkdownToHtml")
    @Mapping(target = "category.slug", source = "categoryName", qualifiedByName = "convertSlug")
    @Mapping(target = "category.name", source = "categoryName")
    BlogInfo toBlogsResponse (BlogTagsInfoInternal blogTagsInfoInternal);

    @Named("convertSlug")
    default String convertSlug(String name){
        return SlugUtils.convertSpaceToHyphen(name);
    }

    @Named("convertMarkdownToHtml")
    default String convertMarkdownToHtml(String description) {
        if (StringUtils.isEmpty(description))
            return "";
        return MarkdownUtils.markdownToHtmlExtensions(description);
    }

    @Named("convertTagToList")
    default List<TagSlugs> convertToTagList(BlogTagsInfoInternal source){
        if (source.getAllTagNames() == null || source.getAllTagNames().isEmpty())
            return List.of();

        String[] names = source.getAllTagNames().split("\\|\\|");
        String[] colors = source.getAllTagColors().split("\\|\\|");

        List<TagSlugs> tags = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            TagSlugs tag = new TagSlugs();
            tag.setSlug(SlugUtils.convertSpaceToHyphen(names[i]));
            tag.setName(names[i].trim());
            tag.setColor(colors[i].trim());
            tags.add(tag);
        }
        return tags;
    }
}
