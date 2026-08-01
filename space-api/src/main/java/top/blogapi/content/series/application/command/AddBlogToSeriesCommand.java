package top.blogapi.content.series.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.series.domain.service.BlogSeriesService;

@Service
@RequiredArgsConstructor
public class AddBlogToSeriesCommand {

    private final BlogSeriesService blogSeriesService;

    @Transactional
    public void execute(Long seriesId, Long blogId, int sortOrder) {
        blogSeriesService.addBlog(seriesId, blogId, sortOrder);
    }
}
