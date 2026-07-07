package top.blogapi.orchestrator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.blogapi.model.entity.Hashtag;
import top.blogapi.service.hashtag.HashtagService;

import java.util.List;

/**
 * Điều phối truy vấn hashtag như lấy danh sách thịnh hành hoặc hàng đầu.
 */
@Component
@RequiredArgsConstructor
public class HashtagOrchestrator {

    private final HashtagService hashtagService;

    public List<Hashtag> getTop(int limit) {
        return hashtagService.getTopHashtags(limit);
    }
}
