package top.blogapi.content.series.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.series.domain.entity.BlogSeries;
import top.blogapi.content.series.domain.service.BlogSeriesService;
import top.blogapi.content.series.interfaces.controller.SeriesController.CreateSeriesRequest;

@Service
@RequiredArgsConstructor
public class CreateSeriesCommand {

    private final BlogSeriesService blogSeriesService;

    @Transactional
    public BlogSeries execute(CreateSeriesRequest request, Long authorId) {
        BlogSeries series = new BlogSeries();
        series.setAuthorId(authorId);
        series.setName(request.name());
        series.setDescription(request.description());
        series.setCoverImage(request.coverImage());
        return blogSeriesService.create(series);
    }
}
