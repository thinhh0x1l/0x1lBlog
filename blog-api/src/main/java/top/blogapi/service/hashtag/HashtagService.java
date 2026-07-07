package top.blogapi.service.hashtag;

import top.blogapi.model.entity.Hashtag;

import java.util.List;

/**
 * Giao diện service quản lý hashtag, hỗ trợ mẫu tìm-hoặc-tạo
 * và truy xuất hashtag thịnh hành.
 */
public interface HashtagService {
    Hashtag findById(Long id);
    Hashtag findOrCreate(String name);
    List<Hashtag> getTopHashtags(int limit);
}
