package top.blogapi.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.entity.Tag;
import top.blogapi.exception.business_exception.domain_exception.TagServiceException;
import top.blogapi.repository.TagRepository;
import top.blogapi.service.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;

    @Override
    public List<Tag> getTagList() {
        return tagRepository.getTagList();
    }

    @Transactional
    @Override
    public int saveTag(Tag tag) {
        return tagRepository.saveTag(tag);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.getTagById(id).orElseThrow(() ->
                TagServiceException.builder()
                        .tagNotExist("BLOG", HttpStatus.BAD_REQUEST,"Tag không tồn tại")
                        .build());
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name).orElseThrow(() ->
                TagServiceException.builder()
                        .tagNotExist("BLOG",HttpStatus.BAD_REQUEST,"Không thể thêm Tag hiện có !!")
                        .build());
    }
}
