package top.blogapi.service.impl.orchestration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.config.RedisKeyConfig;
import top.blogapi.dto.request.tag.CreateTagRequest;
import top.blogapi.dto.request.tag.TagQueryRequest;
import top.blogapi.dto.request.tag.UpdateTagRequest;
import top.blogapi.dto.response._page.TagListPageResponse;
import top.blogapi.dto.response.tag.TagSlugGetBlogsResponse;
import top.blogapi.dto.response.tag.TagResponse;
import top.blogapi.dto.response.tag.TagSlugs;
import top.blogapi.mapper.BlogMapper;
import top.blogapi.mapper.TagMapper;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.model.vo.PageResult;
import top.blogapi.service.RedisService;
import top.blogapi.service.TagService;
import top.blogapi.util.SlugUtils;

import java.util.List;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TagOrchestrator {
    TagService tagService;

    RedisService redisService;

    TagMapper tagMapper;
    BlogMapper blogMapper;

    public TagListPageResponse getTagListPage(TagQueryRequest tagQueryRequest) {
        PageInfo<TagResponse> pageInfo = tagService.getTagList(tagQueryRequest)
                .convert(tagMapper::tagToTagResponse);
        return new TagListPageResponse(pageInfo);
    }

    public List<TagSlugs> getTagSlugList(){
        String redisKey = RedisKeyConfig.TAG_CLOUD_LIST;
        List<TagSlugs> tagListFromRedis = redisService.getListByValue(redisKey, new TypeReference<List<TagSlugs>>() {});
        if(tagListFromRedis == null){
            tagListFromRedis = tagService.getTagList().stream().map(t ->
                    tagMapper.toTagSlugs(t,SlugUtils.convertSpaceToHyphen(t.getName()))
            ).toList();
            redisService.saveListToValue(redisKey,tagListFromRedis);
        }
        return tagListFromRedis;
    }

    public void createTag(CreateTagRequest request){
        tagService.saveTag(request.getTagName(),request.getTagColor());
    }

    public void deleteTagById(Long tagId){
        tagService.deleteTagById(tagId);
    }

    public void updateTag(UpdateTagRequest request){
        tagService.updateTag(request.getTagName(), request.getTagColor(), request.getId());
    }


    public TagSlugGetBlogsResponse tagIdGetBlogsResponse(String slug, Integer pageNum, Integer pageSize){
        String tagName = SlugUtils.convertHyphenToSpace(slug);
        String orderBy = "is_top desc, create_time desc";
        PageHelper.startPage(pageNum,pageSize,orderBy);
        PageInfo<BlogTagsInfo>  blogTagsInfos =
                new PageInfo<>(tagService.getBlogInfoListByTagNameAndIsPublished(tagName));
        Tag tag = tagService.getTagByName(tagName);
        return new TagSlugGetBlogsResponse(
                tagMapper.toTagSlugs(tag,SlugUtils.convertSpaceToHyphen(tag.getName())),
                PageResult.from(blogTagsInfos.convert(blogMapper::toBlogsResponse))
        );
    }
}
