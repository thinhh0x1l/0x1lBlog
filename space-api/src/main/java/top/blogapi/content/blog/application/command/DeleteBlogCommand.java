package top.blogapi.content.blog.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.blogapi.content.blog.domain.service.BlogService;
import top.blogapi.shared.exception.AppException;
import top.blogapi.shared.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class DeleteBlogCommand {

    private final BlogService blogService;

    @Transactional
    public void execute(Long id, Long userId) {
        var blog = blogService.findById(id);
        if (!blog.getAuthorId().equals(userId)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Bạn không có quyền xóa bài viết này");
        }
        blogService.softDelete(id);
    }
}
