package top.blogapi.service;

import top.blogapi.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getTagList();

    int saveTag(Tag tag);

    Tag getTagById(Long id);
}
