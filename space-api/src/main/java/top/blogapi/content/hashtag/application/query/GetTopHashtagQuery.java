package top.blogapi.content.hashtag.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.content.hashtag.domain.entity.Hashtag;
import top.blogapi.content.hashtag.domain.service.HashtagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTopHashtagQuery {

    private final HashtagService hashtagService;

    public List<Hashtag> execute(int limit) {
        return hashtagService.getTopHashtags(limit);
    }
}
