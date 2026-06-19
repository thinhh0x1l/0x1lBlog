package top.blogapi.service.hashtag;

import top.blogapi.model.entity.Hashtag;

import java.util.List;

public interface HashtagService {
    Hashtag findById(Long id);
    Hashtag findOrCreate(String name);
    List<Hashtag> getTopHashtags(int limit);
}
