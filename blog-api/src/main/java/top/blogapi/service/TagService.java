package top.blogapi.service;

import com.github.pagehelper.PageInfo;
import top.blogapi.dto.request.tag.TagQueryRequest;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.BlogTagsInfo;

import java.util.List;

public interface TagService {
    List<Tag> getTagList();

    PageInfo<Tag> getTagList(TagQueryRequest tagQueryRequest);

    Tag saveTag(String name, String color);

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    boolean tagExist (String name);

    void deleteTagById(Long tagId);

    void updateTag(String name, String color, Long id);

    List<Tag> getTagListByBlogId(Long blogId);

    List<BlogTagsInfo> getBlogInfoListByTagNameAndIsPublished(String name);
}
