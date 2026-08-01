package top.blogapi.content.series.application.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.blogapi.content.series.domain.entity.BlogSeries;
import top.blogapi.content.series.domain.service.BlogSeriesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSeriesQuery {

    private final BlogSeriesService blogSeriesService;

    public BlogSeries execute(Long id) {
        return blogSeriesService.findById(id);
    }

    public List<BlogSeries> getByAuthor(Long authorId, int page, int size) {
        return blogSeriesService.getByAuthorId(authorId, page, size);
    }
}
