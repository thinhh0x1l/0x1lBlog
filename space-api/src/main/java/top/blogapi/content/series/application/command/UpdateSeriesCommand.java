package top.blogapi.content.series.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.series.domain.entity.BlogSeries;
import top.blogapi.content.series.domain.service.BlogSeriesService;
import top.blogapi.content.series.interfaces.controller.SeriesController.UpdateSeriesRequest;

@Service
@RequiredArgsConstructor
public class UpdateSeriesCommand {

    private final BlogSeriesService blogSeriesService;

    @Transactional
    public BlogSeries execute(Long id, UpdateSeriesRequest request) {
        BlogSeries series = blogSeriesService.findById(id);
        if (request.name() != null) series.setName(request.name());
        if (request.description() != null) series.setDescription(request.description());
        if (request.coverImage() != null) series.setCoverImage(request.coverImage());
        return blogSeriesService.update(series);
    }
}
