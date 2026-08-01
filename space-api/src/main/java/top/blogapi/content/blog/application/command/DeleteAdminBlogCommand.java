package top.blogapi.content.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.service.BlogService;

@Service
@RequiredArgsConstructor
public class DeleteAdminBlogCommand {

    private final BlogService blogService;

    @Transactional
    public void execute(Long id) {
        blogService.softDelete(id);
    }
}
