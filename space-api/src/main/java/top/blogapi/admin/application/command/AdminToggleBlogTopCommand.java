package top.blogapi.admin.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.admin.domain.repository.AdminBlogRepository;

@Service
@RequiredArgsConstructor
public class AdminToggleBlogTopCommand {

    private final AdminBlogRepository adminBlogRepository;

    @Transactional
    public void execute(Long blogId, boolean isTop) {
        adminBlogRepository.toggleTop(blogId, isTop);
    }
}
