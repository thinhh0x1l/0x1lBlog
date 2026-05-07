package top.blogapi.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.dto.request.tag.TagQueryRequest;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.entity.Tag;
import top.blogapi.model.vo.BlogTagsInfo;
import top.blogapi.repository.TagRepository;
import top.blogapi.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTagList() {
        return tagRepository.getTagList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Tag> getTagList(TagQueryRequest tagQueryRequest) {
        try(Page<Object> page = PageHelper.startPage(
                tagQueryRequest.getPageNum(),
                tagQueryRequest.getPageSize(),
                tagQueryRequest.getSortBy() + " " + tagQueryRequest.getSortOrder())) {
            return new PageInfo<>(
                    tagRepository.getTagList()
            );
        }
    }

    @Transactional
    @Override
    public Tag saveTag(String name, String color) {
            Tag t =new Tag (name,color);
            if(tagRepository.saveTag(t)==0)
                throw new AppException(ErrorCode.INTERNAL_ERROR,"Thêm tag không thành công");
            return  t;
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTagById(Long id) {
        return tagRepository.getTagById(id).orElseThrow(() ->
                new AppException(ErrorCode.TAG_NOT_FOUND)
        );
    }

    @Override
        @Transactional(readOnly = true)
        public Tag getTagByName(String name) {
            return tagRepository.getTagByName(name)
                    .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_FOUND)
                            .addContext("tagName", name));
        }

    @Override
    @Transactional
    public boolean tagExist(String name) {
        return tagRepository.tagExist(name).isPresent();
    }

    @Override
    @Transactional
    public void deleteTagById(Long tagId) {
        if(tagRepository.deleteTagById(tagId) ==0)
            throw new AppException(ErrorCode.TAG_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public void updateTag(String name, String color, Long id) {
       if(tagRepository.updateTag(name, color, id)==0)
           throw new AppException(ErrorCode.TAG_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getTagListByBlogId(Long blogId) {
        return tagRepository.getTagListByBlogId(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogTagsInfo> getBlogInfoListByTagIdAndIsPublished(String name) {
        return tagRepository.getBlogInfoListByTagIdAndIsPublished(name);
    }
}
