package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.admin.domain.repository.AdminBlogRepository;

@Service
@RequiredArgsConstructor
public class AdminToggleBlogRecommendCommand {

    private final AdminBlogRepository adminBlogRepository;

    @Transactional
    public void execute(Long blogId, boolean isRecommend) {
        adminBlogRepository.toggleRecommend(blogId, isRecommend);
    }
}
